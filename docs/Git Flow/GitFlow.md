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