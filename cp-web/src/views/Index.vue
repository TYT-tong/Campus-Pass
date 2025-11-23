<template>
  <div class="welcome">

    <div class="content">
      <div class="sub-title">欢迎体验</div>
      <div class="title">校园通后台管理系统</div>
      <div class="desc"> -- Vue3+ElementPlus+SpringCloud+Mysql打造通用后台管理系统</div>
    </div>
    <div class="img"></div>
  </div>

</template>

<script setup lang="ts">


// 定义组件名称（解决 ESLint multi-word-component-names 错误）
defineOptions({
  name: 'IndexView'
});






</script>


<style lang="scss" scoped>
.welcome {
  display: flex;
  justify-content: center;
  align-content: center;
  width: 100%;
  height: 100%;

  .content {
    position: relative;
    top: 300px;
    right: 200px;

    .sub-title {
      font-size: 30px;
      line-height: 42px;
      color: #333;
    }

    .title {
      font-size: 40px;
      line-height: 62px;
      color: #4091ff;
    }

    .desc {
      position: relative;
      left: 20px;
      text-align: right;
      font-size: 14px;
      color: #999;
    }
  }

  .img {
    position: relative;
    top: 180px;
    margin-left: 105px;
    background-image: url('@/assets/images/welcome.png');
    width: 371px;
    height: 438px;

  }
}
</style>
