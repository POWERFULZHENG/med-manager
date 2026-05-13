# 家庭常用药管理系统 - Git Flow 自动化脚本
# 使用方法: .\scripts\git-flow.ps1 <command> [name]

param(
    [Parameter(Position=0)]
    [string]$Command,
    [Parameter(Position=1)]
    [string]$Name
)

$ProjectName = "家庭常用药管理系统"
$DevelopBranch = "develop"

function Write-ColorOutput {
    param(
        [string]$Text,
        [ConsoleColor]$Color = [ConsoleColor]::White
    )
    Write-Host $Text -ForegroundColor $Color
}

function Write-Banner {
    Write-ColorOutput ""
    Write-ColorOutput "========================================" ([ConsoleColor]::Cyan)
    Write-ColorOutput "  $ProjectName Git Flow 工具" ([ConsoleColor]::Cyan)
    Write-ColorOutput "========================================" ([ConsoleColor]::Cyan)
    Write-ColorOutput ""
}

function Write-Help {
    Write-Banner
    Write-ColorOutput "使用方法:" ([ConsoleColor]::Yellow)
    Write-ColorOutput "  .\scripts\git-flow.ps1 <command> [name]"
    Write-ColorOutput ""
    Write-ColorOutput "可用命令:" ([ConsoleColor]::Yellow)
    Write-ColorOutput "  start                    显示当前分支状态并切换到 develop"
    Write-ColorOutput "  feature <name>           创建新功能分支 feature/name"
    Write-ColorOutput "  hotfix <name>            创建修复分支 hotfix/name"
    Write-ColorOutput "  release <version>        创建发布分支 release/vx.y.z"
    Write-ColorOutput "  commit <message>         提交当前修改 (自动加前缀)"
    Write-ColorOutput "  finish                   完成当前分支并合并到 develop"
    Write-ColorOutput "  sync                     同步 develop 分支最新代码"
    Write-ColorOutput "  status                   显示当前工作区状态"
    Write-ColorOutput "  branches                 列出所有本地分支"
    Write-ColorOutput ""
    Write-ColorOutput "示例:" ([ConsoleColor]::Yellow)
    Write-ColorOutput "  .\scripts\git-flow.ps1 feature medicine-add"
    Write-ColorOutput "  .\scripts\git-flow.ps1 hotfix login-error"
    Write-ColorOutput "  .\scripts\git-flow.ps1 commit '修复登录页面样式'"
    Write-ColorOutput ""
}

function Invoke-GitStatus {
    Write-Banner
    Write-ColorOutput "? 当前工作区状态" ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    git status
    Write-ColorOutput ""
}

function Invoke-Start {
    Write-Banner
    Write-ColorOutput "? 开始开发，切换到 develop 分支..." ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    
    git checkout $DevelopBranch
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "? 切换分支失败" ([ConsoleColor]::Red)
        exit 1
    }
    
    Write-ColorOutput ""
    Write-ColorOutput "? 同步最新代码..." ([ConsoleColor]::Yellow)
    git pull origin $DevelopBranch
    
    Write-ColorOutput ""
    Write-ColorOutput "? 已切换到 $DevelopBranch 分支并更新至最新" ([ConsoleColor]::Green)
    Write-ColorOutput ""
}

function Invoke-Feature {
    param([string]$FeatureName)
    
    if ([string]::IsNullOrEmpty($FeatureName)) {
        Write-ColorOutput "? 请指定功能名称" ([ConsoleColor]::Red)
        Write-ColorOutput "   示例: .\scripts\git-flow.ps1 feature medicine-add" ([ConsoleColor]::Gray)
        exit 1
    }
    
    $BranchName = "feature/$FeatureName"
    
    Write-Banner
    Write-ColorOutput "? 创建功能分支: $BranchName" ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    
    git checkout -b $BranchName $DevelopBranch
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput ""
        Write-ColorOutput "? 分支创建成功！" ([ConsoleColor]::Green)
        Write-ColorOutput "   现在可以在 $BranchName 分支上开发新功能了" ([ConsoleColor]::Gray)
        Write-ColorOutput ""
    } else {
        Write-ColorOutput "? 分支创建失败" ([ConsoleColor]::Red)
        exit 1
    }
}

function Invoke-Hotfix {
    param([string]$FixName)
    
    if ([string]::IsNullOrEmpty($FixName)) {
        Write-ColorOutput "? 请指定修复名称" ([ConsoleColor]::Red)
        Write-ColorOutput "   示例: .\scripts\git-flow.ps1 hotfix login-error" ([ConsoleColor]::Gray)
        exit 1
    }
    
    $BranchName = "hotfix/$FixName"
    
    Write-Banner
    Write-ColorOutput "? 创建修复分支: $BranchName" ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    
    git checkout -b $BranchName $DevelopBranch
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput ""
        Write-ColorOutput "? 分支创建成功！" ([ConsoleColor]::Green)
        Write-ColorOutput "   现在可以在 $BranchName 分支上修复问题了" ([ConsoleColor]::Gray)
        Write-ColorOutput ""
    } else {
        Write-ColorOutput "? 分支创建失败" ([ConsoleColor]::Red)
        exit 1
    }
}

function Invoke-Release {
    param([string]$Version)
    
    if ([string]::IsNullOrEmpty($Version)) {
        Write-ColorOutput "? 请指定版本号" ([ConsoleColor]::Red)
        Write-ColorOutput "   示例: .\scripts\git-flow.ps1 release 1.0.0" ([ConsoleColor]::Gray)
        exit 1
    }
    
    $BranchName = "release/v$Version"
    
    Write-Banner
    Write-ColorOutput "? 创建发布分支: $BranchName" ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    
    git checkout -b $BranchName $DevelopBranch
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput ""
        Write-ColorOutput "? 分支创建成功！" ([ConsoleColor]::Green)
        Write-ColorOutput "   现在可以在 $BranchName 分支上进行发布前测试" ([ConsoleColor]::Gray)
        Write-ColorOutput ""
    } else {
        Write-ColorOutput "? 分支创建失败" ([ConsoleColor]::Red)
        exit 1
    }
}

function Invoke-Commit {
    param([string]$Message)
    
    if ([string]::IsNullOrEmpty($Message)) {
        Write-ColorOutput "? 请提交信息" ([ConsoleColor]::Red)
        Write-ColorOutput "   示例: .\scripts\git-flow.ps1 commit '修复登录页面样式'" ([ConsoleColor]::Gray)
        exit 1
    }
    
    $CurrentBranch = git rev-parse --abbrev-ref HEAD
    $Prefix = ""
    
    if ($CurrentBranch -like "feature/*") {
        $Prefix = "feat"
    } elseif ($CurrentBranch -like "hotfix/*") {
        $Prefix = "fix"
    } elseif ($CurrentBranch -like "release/*") {
        $Prefix = "release"
    } else {
        $Prefix = "chore"
    }
    
    $FullMessage = "${Prefix}: ${Message}"
    
    Write-Banner
    Write-ColorOutput "? 提交修改" ([ConsoleColor]::Yellow)
    Write-ColorOutput "   分支: $CurrentBranch" ([ConsoleColor]::Gray)
    Write-ColorOutput "   信息: $FullMessage" ([ConsoleColor]::Gray)
    Write-ColorOutput ""
    
    git add .
    git commit -m $FullMessage
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput ""
        Write-ColorOutput "? 提交成功！" ([ConsoleColor]::Green)
        Write-ColorOutput ""
    } else {
        Write-ColorOutput "? 提交失败" ([ConsoleColor]::Red)
        exit 1
    }
}

function Invoke-Finish {
    Write-Banner
    
    $CurrentBranch = git rev-parse --abbrev-ref HEAD
    
    if ($CurrentBranch -eq $DevelopBranch -or $CurrentBranch -eq "main") {
        Write-ColorOutput "? 不能在 $CurrentBranch 分支上执行 finish" ([ConsoleColor]::Red)
        exit 1
    }
    
    Write-ColorOutput "? 完成分支: $CurrentBranch" ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    
    Write-ColorOutput "? 推送当前分支到远程..." ([ConsoleColor]::Gray)
    git push origin $CurrentBranch
    
    Write-ColorOutput ""
    Write-ColorOutput "? 切换到 $DevelopBranch 并合并..." ([ConsoleColor]::Gray)
    git checkout $DevelopBranch
    git merge --no-ff $CurrentBranch -m "Merge branch '$CurrentBranch' into $DevelopBranch"
    
    if ($LASTEXITCODE -eq 0) {
        Write-ColorOutput ""
        Write-ColorOutput "? 合并成功！" ([ConsoleColor]::Green)
        Write-ColorOutput "   已将 $CurrentBranch 合并到 $DevelopBranch" ([ConsoleColor]::Gray)
        Write-ColorOutput ""
        Write-ColorOutput "? 建议: 前往 GitHub 发起 Pull Request 进行代码审查" ([ConsoleColor]::Cyan)
        Write-ColorOutput ""
    } else {
        Write-ColorOutput "? 合并失败，请处理冲突后重试" ([ConsoleColor]::Red)
        exit 1
    }
}

function Invoke-Sync {
    Write-Banner
    Write-ColorOutput "? 同步 $DevelopBranch 分支最新代码..." ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    
    $CurrentBranch = git rev-parse --abbrev-ref HEAD
    
    git checkout $DevelopBranch
    git pull origin $DevelopBranch
    
    if ($CurrentBranch -ne $DevelopBranch) {
        Write-ColorOutput ""
        Write-ColorOutput "??  切回原分支: $CurrentBranch" ([ConsoleColor]::Gray)
        git checkout $CurrentBranch
        
        Write-ColorOutput ""
        Write-ColorOutput "? 将最新的 $DevelopBranch 合并到当前分支..." ([ConsoleColor]::Gray)
        git merge $DevelopBranch
    }
    
    Write-ColorOutput ""
    Write-ColorOutput "? 同步完成！" ([ConsoleColor]::Green)
    Write-ColorOutput ""
}

function Invoke-ListBranches {
    Write-Banner
    Write-ColorOutput "? 所有本地分支" ([ConsoleColor]::Yellow)
    Write-ColorOutput ""
    git branch -a
    Write-ColorOutput ""
}

switch ($Command) {
    "help" { Write-Help }
    "start" { Invoke-Start }
    "feature" { Invoke-Feature $Name }
    "hotfix" { Invoke-Hotfix $Name }
    "release" { Invoke-Release $Name }
    "commit" { Invoke-Commit $Name }
    "finish" { Invoke-Finish }
    "sync" { Invoke-Sync }
    "status" { Invoke-GitStatus }
    "branches" { Invoke-ListBranches }
    default { Write-Help }
}
