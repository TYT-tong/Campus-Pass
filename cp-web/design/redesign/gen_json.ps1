$ErrorActionPreference = 'Stop'
$svgHome = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/home.svg'))
$svgUser = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/user.svg'))
$svgRole = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/role.svg'))
$svgRecords = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/records.svg'))
$svgSidebar = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/sidebar.svg'))
$svgBreadcrumb = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/breadcrumb.svg'))
$svgNotice = [Convert]::ToBase64String([IO.File]::ReadAllBytes('design/redesign/notice.svg'))
$json = '{"主页":"'+$svgHome+'","用户管理页":"'+$svgUser+'","角色管理页":"'+$svgRole+'","消费记录查询页":"'+$svgRecords+'","侧边栏样式":"'+$svgSidebar+'","面包屑样式":"'+$svgBreadcrumb+'","通知公告页":"'+$svgNotice+'"}'
Set-Content -Path 'design/redesign/output.json' -Value $json -Encoding UTF8