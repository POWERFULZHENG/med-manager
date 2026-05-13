✅ Git Flow 版本管理脚本已创建并提交！

📦 文件位置
文件	    位置
脚本文件	scripts/git-flow.ps1
快捷命令	frontend/med-manager-front/package.json

🚀 使用方式
方式一：使用 npm 快捷命令（推荐）
在 frontend/med-manager-front 目录下执行：
命令	                            功能
npm run git:start	                切换到 develop 并同步最新代码
npm run git:feature -- xxx	        创建 feature/xxx 分支
npm run git:hotfix -- xxx	        创建 hotfix/xxx 分支
npm run git:release -- 1.0.0	    创建 release/v1.0.0 分支
npm run git:commit -- '提交信息'	 自动加前缀提交
npm run git:finish	                合并当前分支到 develop
npm run git:sync	                同步 develop 最新代码
npm run git:status	                查看工作区状态
npm run git:branches	            列出所有分支
方式二：直接调用脚本
在项目根目录执行：
PowerShell
运行
# 创建新功能分支.\scripts\git-flow.ps1 feature medicine-search
# 创建修复分支.\scripts\git-flow.ps1 hotfix login-bug
# 提交代码（自动识别分支类型加前缀）.\scripts\git-flow.ps1 commit "添加搜索功能"
# 完成分支并合并到 develop.\scripts\git-flow.ps1 finish
✨ 脚本自动功能
功能	                            说明
🎯 自动加 commit 前缀	            feature/* → feat: hotfix/* → fix:
🔄 同步代码	                        finish 前自动拉取最新 develop
📝 彩色输出	                        不同状态不同颜色提示
✅ 合并保护	                       禁止在 main/develop 执行 finish
🚫 参数校验	                        缺少参数时自动提示正确用法  

📚 Git Flow 工作流程（按脚本设计）
开始开发
    ↓
git:start → 切到 develop + 拉最新
    ↓
创建分支
    ├─ git:feature xxx → 新功能
    ├─ git:hotfix xxx  → 修bug
    └─ git:release x.y.z → 发布
    ↓
开发中...
    ↓
git:commit '修改内容' （自动加前缀）
    ↓
git:finish → 合并回 develop + 推远程

脚本已推送到 GitHub develop 分支，团队成员拉取后即可直接使用！🎉