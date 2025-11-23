-- 取消部门/岗位相关功能的补丁脚本
-- 1) 删除部门/岗位菜单及按钮
DELETE FROM sys_role_menu WHERE menu_id IN (103,104,1016,1017,1018,1019,1020,1021,1022,1023,1024);
DELETE FROM sys_menu      WHERE menu_id IN (103,104,1016,1017,1018,1019,1020,1021,1022,1023,1024);

-- 2) 删除与部门/岗位相关的表
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_user_post;
DROP TABLE IF EXISTS sys_post;
DROP TABLE IF EXISTS sys_dept;

-- 3) 删除用户表中的部门字段
ALTER TABLE sys_user DROP COLUMN dept_id;