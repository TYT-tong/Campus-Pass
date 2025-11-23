<template>
  <div style="padding: 20px;">
    <h3>SvgIcon 组件测试</h3>
    
    <div style="margin: 20px 0;">
      <p>测试 validCode 图标：</p>
      <SvgIcon icon-class="validCode" :size="24" />
      <span style="margin-left: 10px;">validCode 图标</span>
    </div>
    
    <div style="margin: 20px 0;">
      <p>测试 user 图标：</p>
      <SvgIcon icon-class="user" :size="24" />
      <span style="margin-left: 10px;">user 图标</span>
    </div>
    
    <div style="margin: 20px 0;">
      <p>测试 lock 图标：</p>
      <SvgIcon icon-class="lock" :size="24" />
      <span style="margin-left: 10px;">lock 图标</span>
    </div>
    
    <div style="margin: 20px 0; color: red;">
      <p>调试信息：</p>
      <p>SymbolId: {{ testSymbolId }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import SvgIcon from './index.vue'

const testSymbolId = computed(() => {
  return `icon-validCode`
})
</script>