# cc

#### 介绍
才财新功能的代码仓库

#### 软件架构
后端技术栈：JDK 17.0.15,Spring Boot 3.4.5,Spring AI，MyBatis-plus

前端技术栈：html,css,js,Vue3,vite,pinia,axios

服务器数据库：MySQL 8.4.5,Redis 7.4.3


#### 安装教程
1.  克隆项目到本地：git clone https://gitee.com/dongguan-caicai-network/cc-code-repository.git

https://gitee.com/curry-mike/cc-code-repository-master.git

2.  运行项目


#### 如何提交自己的代码？

1.  从 develop 分支创建 feature 分支，命名规则为 （示例）feature/xxx_250512_login

```
git checkout develop
git pull origin develop  （确保是最新的）
git checkout -b feature/xxx
```

2.  在自己的 feature 分支上开发并提交

```
git add .
git commit -m "实现了用户登录功能"
```

3.  定期从 develop 拉取最新变更并合并（避免冲突）

```
git checkout develop
git pull origin develop
git checkout feature/xxx
git merge develop
```

4.  开发完成后合并回 develop

```
git push origin feature/xxx
在 Gitee 上发起 Merge Request（MR）或 Pull Request（PR），由团队审核后合并到 develop
```

5.  合并完成后，可以删除 feature 分支（可选）

```
git branch -d feature/xxx        # 删除本地分支
git push origin --delete feature/xxx  # 删除远程分支
```