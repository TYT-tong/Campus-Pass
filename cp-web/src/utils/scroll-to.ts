/**
 * 平滑滚动到指定位置（TypeScript版本）
 */

// 扩展Math对象，添加缓动算法
declare global {
  interface Math {
    easeInOutQuad(t: number, b: number, c: number, d: number): number
  }
}

/**
 * 二次缓动算法（先加速后减速）
 * 用于计算滚动过程中的位置，使滚动效果更自然
 */
Math.easeInOutQuad = function (t: number, b: number, c: number, d: number): number {
  // 将当前时间转换为总时长的占比（除以总时长的一半，用于分段处理）
  t /= d / 2
  // 前半段（t < 1）：加速阶段，使用二次函数计算位置（曲线上升）
  if (t < 1) {
    return (c / 2) * t * t + b
  }
  // 后半段（t >= 1）：减速阶段，调整t的值后计算位置（曲线下降）
  t--
  return (-c / 2) * (t * (t - 2) - 1) + b
}

/**
 * 兼容各浏览器的动画帧请求函数
 * 用于高效执行动画，由浏览器控制刷新频率（通常60帧/秒），比setTimeout更流畅
 */
const requestAnimFrame = (function (): (callback: FrameRequestCallback) => number {
  return (
    window.requestAnimationFrame || // 标准浏览器
    (window as any).webkitRequestAnimationFrame || // Chrome/Safari
    (window as any).mozRequestAnimationFrame || // Firefox
    // 降级方案：若浏览器不支持动画帧，则用setTimeout模拟（1000/60 ≈ 16.7ms/帧）
    function (callback: FrameRequestCallback): number {
      return window.setTimeout(callback, 1000 / 60)
    }
  )
})()

/**
 * 滚动页面到指定位置（兼容不同浏览器的滚动元素）
 * 不同浏览器的滚动容器可能不同（html、body或body的父节点），这里统一处理
 */
function move(amount: number): void {
  document.documentElement.scrollTop = amount // 标准模式下的滚动容器（html元素）
  const bodyParent = document.body.parentNode as Element
  if (bodyParent) {
    bodyParent.scrollTop = amount // 兼容某些旧浏览器（body的父节点）
  }
  document.body.scrollTop = amount // 怪异模式下的滚动容器（body元素）
}

/**
 * 获取当前页面的滚动位置（兼容不同浏览器）
 */
function position(): number {
  return (
    document.documentElement.scrollTop || // 优先取html元素的滚动值
    (document.body.parentNode as Element)?.scrollTop || // 兼容旧浏览器
    document.body.scrollTop // 兼容怪异模式
  )
}

/**
 * 平滑滚动到指定位置的核心函数
 * @param to - 目标滚动位置（像素值）
 * @param duration - 滚动总时长（毫秒，默认500ms）
 * @param callback - 滚动完成后的回调函数（可选）
 */
export function scrollTo(
  to: number,
  duration?: number,
  callback?: () => void
): void {
  const start = position() // 获取初始滚动位置
  const change = to - start // 计算需要滚动的总距离（变化量）
  const increment = 20 // 每次动画帧的时间间隔（毫秒）
  let currentTime = 0 // 记录当前已执行的动画时间
  // 处理默认时长：若未传入duration，默认500ms
  duration = typeof duration === 'undefined' ? 500 : duration

  /**
   * 动画执行函数：逐帧更新滚动位置
   */
  const animateScroll = function (): void {
    // 累加已执行时间（每次调用增加20ms）
    currentTime += increment
    // 用缓动算法计算当前时间点的滚动位置
    const val = Math.easeInOutQuad(currentTime, start, change, duration!)
    // 移动页面到计算出的位置
    move(val)
    // 判断动画是否结束：若当前时间小于总时长，继续请求下一帧
    if (currentTime < duration!) {
      requestAnimFrame(animateScroll)
    } else {
      // 动画结束后，若有回调函数则执行
      if (callback && typeof callback === 'function') {
        callback()
      }
    }
  }

  // 启动动画
  animateScroll()
}