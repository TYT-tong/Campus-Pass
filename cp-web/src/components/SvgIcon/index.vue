<template>
  <svg :class="['svg-icon', className]" :width="size" :height="size" :style="customStyle" aria-hidden="true">
    <!-- 引用 SVG Sprite 中的图标（与插件配置的 symbolId 对应） -->
    <use :xlink:href="`#${symbolId}`" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from "vue";

// 定义组件名称（解决 ESLint multi-word-component-names 错误）
defineOptions({
  name: 'SvgIcon'
});

// 组件参数：支持图标名、尺寸、自定义样式
const props = defineProps({
  iconClass: { // 图标名（对应 svg 目录下的文件名，如 captcha、user/login）
    type: String,
    required: true,
    default: "",
  },
  size: { // 图标尺寸（默认 16px，适配输入框）
    type: [Number, String],
    default: 16,
  },
  className: { // 自定义样式类
    type: String,
    default: "",
  },
  customStyle: { // 自定义内联样式（如颜色）
    type: Object,
    default: () => ({}),
  },
});

// 计算 SVG Sprite 的 symbolId（与插件配置的格式一致）
const symbolId = computed(() => {
  return `icon-${props.iconClass.replace(/\//g, "-")}`; // 子目录替换为连字符（如 user/login → icon-user-login）
});
</script>

<style scoped>
.svg-icon {
  vertical-align: -0.15em;
  /* 微调图标与文字对齐 */
  fill: currentColor;
  /* 继承父元素颜色（适配 Element Plus 主题） */
  overflow: hidden;
}
</style>
