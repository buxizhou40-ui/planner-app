# 任务规划 App —— 打包成 APK 的步骤

这是一个 WebView 外壳工程，把你手机上用的那个网页（`assets/index.html`）
包成可以安装的安卓 App。

编译不在你电脑上做，而是在 **GitHub 的服务器**上做——因为
`dl.google.com`（Android SDK 的下载源）在你这网络不通，而 GitHub 的
服务器在国内可以访问。

---

## 第一步：注册 GitHub（5 分钟）

1. 打开 https://github.com/signup
2. 填邮箱、密码、用户名
3. 收验证邮件，点激活
4. 登录

---

## 第二步：建仓库（2 分钟）

1. 打开 https://github.com/new
2. **Repository name** 填：`planner-app`
3. 选 **Public**（公开仓库不消耗免费额度）
4. 不要勾选任何 "Initialize this repository with..." 选项
5. 点 **Create repository**

---

## 第三步：上传文件（3 分钟）

新建的仓库页面会出现一行提示 "Quick setup — if you've done this kind of thing before"，
下面有一行链接：**uploading an existing file**。

1. 点 **uploading an existing file**
2. 把本文件夹（`android-app`）里的**所有内容**拖进浏览器窗口
   - 注意：拖的是**里面的文件和文件夹**（`app`、`.github`、`build.gradle` 等），
     不是 `android-app` 这个外壳文件夹本身
   - Chrome / Edge 支持直接拖文件夹，会自动展开
   - **`.github` 是隐藏文件夹**，如果拖拽时漏掉了，需要在网页上手动新建：
     点 "choose your files" 后再补充，或者用下面的命令行方式
3. 页面底部 Commit changes 填个说明（比如 `init`），点 **Commit changes**

> 如果拖拽总是漏文件，用命令行更稳：
> ```
> git init
> git add .
> git commit -m "init"
> git branch -M main
> git remote add origin https://github.com/你的用户名/planner-app.git
> git push -u origin main
> ```
> （需要电脑上装了 Git）

---

## 第四步：等它编译（5–8 分钟）

1. 上传完成后，点仓库顶部的 **Actions** 标签
2. 会看到一个叫 **Build APK** 的任务在跑（黄点 → 转圈）
3. 等它变成 **绿色对勾**

期间可以点进去看实时日志，卡在哪一步一目了然。

---

## 第五步：下载 APK

1. 点进那个绿色的运行记录
2. 页面最下方找到 **Artifacts** 区域
3. 点 **planner-apk** 下载（会得到一个 zip）
4. 解压出 `app-debug.apk`
5. 传到平板 → 点安装（首次会提示"允许安装未知来源应用"，允许即可）

---

## 以后想改功能怎么办

改完 `app/src/main/assets/index.html`（就是那个网页），在 GitHub 网页上
重新上传覆盖，Actions 会**自动重新编译**，你再下载新 APK 就行。

也可以用 Release 打包：把 APK 上传到仓库的 Releases 页面，得到固定下载链接。

---

## 关于签名

现在打的是 **debug 包**，自带调试签名，**日常自己用完全没问题**。

如果以后要发布到应用商店，需要生成正式的 keystore 并配置 `signingConfigs`——
那是另一套流程，到时候再说。

---

## 工程结构

```
android-app/
├── .github/workflows/build.yml    GitHub 自动编译配置
├── build.gradle                   根构建脚本
├── settings.gradle                模块声明
├── gradle.properties
└── app/
    ├── build.gradle               应用配置（包名、版本、SDK 版本）
    └── src/main/
        ├── AndroidManifest.xml    应用声明（图标、名称、入口）
        ├── java/com/zhou/planner/
        │   └── MainActivity.java  WebView 外壳
        ├── res/
        │   ├── values/strings.xml 应用名
        │   └── mipmap-*/          图标（5 种尺寸）
        └── assets/                你的网页全部内容
            ├── index.html
            ├── icon-*.png
            └── bg/*.jpg           8 张背景图
```
