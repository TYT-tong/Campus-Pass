// src/env.d.ts
/// <reference types="vite/client" />

declare module '*.scss' {
  export const sidebarBackground: string;
  export const menuColor: string;
  export const menuLightColor: string;
  export const menuColorActive: string;
  export const menuBackground: string;
  export const menuLightBackground: string;
  export const subMenuBackground: string;
  export const subMenuHover: string;
  export const sideBarWidth: string;
  export const logoTitleColor: string;
  export const logoLightTitleColor: string;
  export const menuBg: string;
  export const menuText: string;
  export const menuActiveText: string;
}

declare module '*.module.scss' {
  const classes: Record<string, string>
  export default classes
}
