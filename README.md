# authority-system-demo

本项目为个人练手项目。目的是个人对Java后端开发的流程与编码更加熟悉

本项目为权限管理系统，旨在组织中进行后台系统权限管理。

本项目参考b站的项目：https://www.bilibili.com/video/BV1dt4y1x7rT

前端采用vue-admin模板进行修改

由于不会将前端项目和后端项目合并，所以删除了之前的仓库，重新提交

后端技术栈：

- Spring Boot
- MyBatis Plus
- Spring Security
- Jwt
- Redis
- MySQL

前端技术栈：

- HTML 5
- CSS 3
- JavaScript
- Vue
- Element UI

## 截图与讲解

1、这是登录页面，进行token的验证，任何操作都需要带着token

![登陆界面](https://github.com/arecture/authority-system-demo/blob/main/image/%E7%99%BB%E9%99%86%E7%95%8C%E9%9D%A2.png)

2、这是管理员页面（管理员权限），可以对其他用户进行权限控制，部门、人员增删

![管理员页面](https://github.com/arecture/authority-system-demo/blob/main/image/%E7%AE%A1%E7%90%86%E5%91%98%E9%A1%B5%E9%9D%A2.png)

3、这是李四（普通用户权限），仅可以对系统管理进行操作

![普通用户页面](https://github.com/arecture/authority-system-demo/blob/main/image/%E6%99%AE%E9%80%9A%E7%94%A8%E6%88%B7%E9%A1%B5%E9%9D%A2.png)

4、这是部门管理的页面，可以对部门进行增删改

![部门增删改页面](https://github.com/arecture/authority-system-demo/blob/main/image/%E9%83%A8%E9%97%A8%E5%A2%9E%E5%88%A0%E6%94%B9%E9%A1%B5%E9%9D%A2.png)
