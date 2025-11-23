<template>
  <div class="outerContainer" v-loading="loading">
    <el-row :gutter="20" class="full-height">
      <el-col :span="8" class="full-height">
        <el-card class="full-height">
          <template #header>
            <div class="card-header">
              <el-icon>
                <Memo />
              </el-icon>
              <span>缓存列表</span>
            </div>

            <el-table :data="cacheList" class="custom-table" @row-click="rowClick">
              <el-table-column width="60px" prop="num" label="序号" />
              <el-table-column prop="name" label="缓存名称" />
              <el-table-column prop="info" label="备注" />
              <el-table-column width="60px" label="操作">
                <el-button type="primary" :icon="Delete" style="width: 25px;" />
              </el-table-column>
            </el-table>
          </template>
        </el-card>

      </el-col>

      <el-col :span="8" class="full-height">
        <el-card class="full-height">
          <template #header>
            <div class="card-header">
              <span>键名列表</span>
            </div>
            <el-table :data="keyList" class="custom-table" @row-click="keysDataClick" style="min-width: 400px;">
              <el-table-column width="60px" prop="num" label="序号" />
              <el-table-column prop="name" label="缓存键名" style="text-align: center;" />
              <el-table-column width="60px" label="操作">
                <el-button type="primary" :icon="Delete" style="width: 25px;" />
              </el-table-column>
            </el-table>
          </template>
        </el-card>
      </el-col>


      <el-col :span="8" class="full-height">
        <el-card class="full-height">
          <template #header>
            <div class="card-header">
              <span>缓存内容</span>
            </div>

            <el-form label-position="top">
              <el-form-item label="缓存名称">
                <el-input readonly v-model="data1" />
              </el-form-item>
              <el-form-item label="缓存键名">
                <el-input readonly v-model="data2" />
              </el-form-item>
              <el-form-item label="缓存内容">
                <el-input type="textarea" readonly v-model="data3" />
              </el-form-item>
            </el-form>

          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { Delete } from '@element-plus/icons-vue'
import { getCacheList, getCacheKey } from '@/api/monitor/cache'


const loading = ref(false);
const data1 = ref("");
const data2 = ref("");
const data3 = ref("");

const cacheList = [
  {
    num: 1,
    name: 'login_tokens',
    info: '用户登录缓存',
  },
  {
    num: 2,
    name: 'sys_config',
    info: '配置信息缓存',
  },
  {
    num: 3,
    name: 'sys_dict',
    info: '数据字典',
  },

  {
    num: 4,
    name: 'captcha_codes',
    info: '验证码',
  },
  {
    num: 5,
    name: 'pwd_err_cnt',
    info: '密码错误次数',
  },
];

const keyList = ref([])


const rowClick = (row: any) => {
  console.log("row" + JSON.stringify(row));

  const cacheName = row.name
  loading.value = true
  getCacheList(cacheName).then((response) => {
    console.log("res" + JSON.stringify(response));

    const data = (response as any).rows;
    keyList.value = data.map((item: any, index: number) => {
      const name = item.split(":")[1]
      return {
        cacheName,
        num: index + 1,
        name
      }
    })
    loading.value = false
  }).catch(() => {
    loading.value = false
  });
}


const keysDataClick = (row: any) => {
  data1.value = row.cacheName
  data2.value = row.name
  loading.value = true
  getCacheKey(row.cacheName + ":" + row.name).then((response) => {

    data3.value = response.msg
  }).catch(() => {
    data3.value = "获取缓存内容失败"
  });
  loading.value = false
}

</script>

<style lang="scss" scoped>
.outerContainer {
  height: calc(100vh - 120px);
  margin: 10px;
  font-size: larger;

  .full-height {
    height: 100%;
    // background: red;
  }
}

.card-header {
  height: 100%;

  .el-icon {
    width: 1em;
    height: 1em;
    position: relative;
    top: 0.15625rem; // 使用 rem 替代 px，更适配不同屏幕（假设 1rem = 16px）
  }
}

// 注意：scoped 样式中需使用 ::v-deep 穿透组件内部样式
::v-deep .custom-table {
  font-size: 14px; // 调整字体大小（根据需求修改）
  width: 100%;
  min-width: 400px;


  // 表头文字居中
  th>.cell {
    text-align: center !important;
  }

  // 表格内容文字居中
  td>.cell {
    text-align: center !important;
  }
}
</style>
