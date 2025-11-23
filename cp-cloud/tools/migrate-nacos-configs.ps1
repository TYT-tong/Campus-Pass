Param(
  [Parameter(Mandatory=$true)][string]$SqlPath,
  [string]$Server = "http://192.168.253.131:8848",
  [string]$Username = "nacos",
  [string]$Password = "nacos",
  [string]$DefaultGroup = "DEFAULT_GROUP",
  [string]$DefaultTenant = "public"
)

function Parse-InsertLine {
  param([string]$line)
  $m = [regex]::Match($line, "INSERT\s+INTO\s+config_info\s*\((.*?)\)\s*VALUES\s*\((.*)\)", 'IgnoreCase')
  if (-not $m.Success) { return $null }
  $cols = $m.Groups[1].Value.Split(',').ForEach({ $_.Trim() })
  $valsText = $m.Groups[2].Value
  # SQL value tokenizer respecting quotes
  $vals = @()
  $i = 0
  while ($i -lt $valsText.Length) {
    while ($i -lt $valsText.Length -and ([char]::IsWhiteSpace($valsText[$i]) -or $valsText[$i] -eq ',')) { $i++ }
    if ($i -ge $valsText.Length) { break }
    if ($valsText[$i] -eq "'") {
      $i++
      $sb = New-Object System.Text.StringBuilder
      while ($i -lt $valsText.Length) {
        if ($valsText[$i] -eq "'" ) {
          if ($i+1 -lt $valsText.Length -and $valsText[$i+1] -eq "'") { $sb.Append("'"); $i+=2; continue } else { $i++; break }
        } else { $sb.Append($valsText[$i]); $i++ }
      }
      $vals += $sb.ToString()
    } else {
      $start = $i
      while ($i -lt $valsText.Length -and $valsText[$i] -ne ',') { $i++ }
      $vals += $valsText.Substring($start, $i-$start).Trim()
    }
  }
  $map = @{}
  for ($j=0; $j -lt $cols.Count -and $j -lt $vals.Count; $j++) { $map[$cols[$j].ToLower()] = $vals[$j] }
  return $map
}

Write-Host "Migrating configs from $SqlPath to $Server ..."
if (-not (Test-Path $SqlPath)) { Write-Error "SQL file not found: $SqlPath"; exit 1 }

$lines = Get-Content -Path $SqlPath -Encoding UTF8
$count = 0
foreach ($line in $lines) {
  if ($line -notmatch "(?i)^\s*INSERT\s+INTO\s+config_info") { continue }
  $map = Parse-InsertLine -line $line
  if ($null -eq $map) { continue }
  $dataId   = $map['data_id']
  if ($map.ContainsKey('group_id')) { $groupId = $map['group_id'] } else { $groupId = $DefaultGroup }
  if ($map.ContainsKey('tenant_id')) { $tenantId = $map['tenant_id'] } else { $tenantId = $DefaultTenant }
  $content  = $map['content']
  if ([string]::IsNullOrWhiteSpace($dataId) -or [string]::IsNullOrWhiteSpace($content)) { continue }
  $encoded = [uri]::EscapeDataString($content)
  $body = "dataId=$dataId&group=$groupId&type=yaml&content=$encoded&tenant=$tenantId&username=$Username&password=$Password"
  try {
    $resp = Invoke-WebRequest -Uri "$Server/nacos/v1/cs/configs" -Method POST -ContentType "application/x-www-form-urlencoded" -Body $body -UseBasicParsing
    if ($resp.Content -match "true|ok") { $count++ ; Write-Host ("Pushed " + $dataId + " (" + $groupId + "/" + $tenantId + ")") } else { Write-Warning ("Push failed for " + $dataId + ": " + $resp.Content) }
  } catch { Write-Warning ("Error pushing " + $dataId + ": " + $_.Exception.Message) }
}

Write-Host "Completed. Pushed $count configs."