# GIT使用

###常用命令

以下命令之前都需要有git

| 常用命令                |                                                          |
| ----------------------- | -------------------------------------------------------- |
| init                    | 初始化当前目录为版本库                                   |
| add                     | 把文件添加到仓库                                         |
| commit                  | 把修改过的文件提交到仓库                                 |
| commit -m " "           | 引号中为提示这次做了哪些修改                             |
| status                  | 查看版本库的状态                                         |
| diff                    | 查看commit 后有哪些difference                            |
|  diff HEAD --"文件名"	|  查看与最新版本的区别                                     |
| log                     | 查看提交的各个版本                                       |
| log --pretty=oneline    | 只显示一行的简要信息                                     |
| reset --hard HEAD^      | 回退到前一个版本                                         |
| reset --hard HEAD^^     | 回退到上上个                                             |
| reset --hard HEAD~100   | 回退100个版本                                            |
| reflog                  | 记忆你的每次命令，可以查看版本号，确定到达未来的哪个版本 |
| reset --hard [ID版本号] | 回退到指定的版本                                         |
| checkout --"文件名" | 丢弃此次修改 |
| reset HEAD <文件名> // git reset HEAD readme.txt | 丢弃此次add后的修改 |
| rm | 删除文件 |
| checkout | 将版本库中版本替换工作区版本 |
| branch | 查看分支 |
| branch <name> | 创建分支 |
| checkout <name> | 切换分支 |
| checkout -b <name> | 创建切换分支 |
| merge <name> | 合并分支到当前分支 |
| branch -d <name> | 删除分支 |
| log --graph | 查看版本的分支图 |
| merge --no-ff  -m " message"  dev | 禁止FF模式合并 |
| remote rename "oldname"  "newname" | 远程仓库重命名 |
| remote remove  “name” | 删除某远程仓库 |
| push origin master | 推送master到远程仓库origin上 |
| pull | 拉取最新的提交 |
| checkout -b dev origin/dev | 创建本地分支与远程库的某分支相对应 |
| branch --set-upstream-to=orgin/dev dev | 设置上游的分支   与 本地的分支相对应 |
| rebase | 整理提交的log图为直线 |
|  |  |

注意：文本文件不要用windows自带的文本文件

![1543231952754](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543231952754.png)

### 创建远程库 github

使用SSH连接

1、在用户主目录下

```cmd
ssh-keygen -t rsa -C "youremail@example.com"
```

此时用户主目录下有  .ssh文件夹，里面有id_rsa 和 id_rsa.pub 分别为私钥和公钥

2、登录github，在account settings 下  add ssh key

key文本框里添加id_rsa.pub文件的内容

3.gitbub下创建一个repository， 比如learngit

4.在本地仓库下 

```cmd
git remote add origin git@github.com:(账户名)/(repository名).git
```

origin 则是本机上给远程库的名字

5 

```cmd
git push -u origin master 
```

第一次使用-u参数，会把本地本地的`master`分支内容推送的远程新的`master`分支，还会把本地的`master`分支和远程的`master`分支关联起来，在以后的推送或者拉取时就可以简化命令。

6.之后可以通过

```cmd
git push origin master
```

![1543235727677](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543235727677.png)



### 克隆远程库

```cmd
git clone git@github.com:Damonphysics/gitskills.git	
```

（克隆在当前目录下）

```cmd
cd gitskills
ls
```

 git支持多种协议，但原生的ssh协议比https（可能只开放某些端口，每次需要输入口令）更快

### 创建与合并分支

严格来说head指向master，master指向提交，所以head指向的就是当前分支。

![1543236521122](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543236521122.png)

创建新的分支时，例如```dev```时，git新建了一个指针交dev，指向master的相同提交，再把head指向dev上



![1543236621016](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543236621016.png)

从现在开始，对工作区的修改和提交就是针对dev分支了，比如新提交一次后，dev往前移动一步，而master指针不变

![1543236736306](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543236736306.png)

如果在dev的工作完成了，就可以把dev合并到master上。（将master指向dev）

![1543236846552](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543236846552.png)

合并完分支后，甚至可以删除`dev`分支。删除`dev`分支就是把`dev`指针给删掉，删掉后，我们就剩下了一条`master`分支：

![1543236890312](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543236890312.png)

```
git checkout -b dev
```

-b 表示创建并切换

相当于

```cmd
git branch dev
git checkout dev
```

然后使用

```cmd
git branch
```

查看当前的分支，当前分之前有*号

然后便可以在当前分支上做修改

切换回master

```cmd
git checkout master
```

将dev分支工作合并到master上

```cmd
git merge dev
```

删除dev分支

```cmd
git branch -d dev
```

#### 采用分支工作更安全

### 解决冲突

创建新的分支

```cmd
git checkout -b feature1
```

做出修改,commit后，再切换到master

```cmd
git checkout master
```

做出不同修改,commit后

![1543238549075](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543238549075.png)

```cmd
git merge feature1
```

会提示有文件发生冲突

可以用status查看冲突文件

然后手动解决冲突，再提交

```cmd
git add readme.txt
git commit -m "conflict fixed"
```

![1543238761862](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543238761862.png)

可以用以下命令查看分支的合并情况

```cmd
git log --graph --pretty=oneline --abbrev-commit	
```

最后删除feature1分支

```cmd
git branch -d feature1
```

### 分支管理策略

合并分支时，如果可能git使用fast forward模式，这种模式下删除分支后，会丢掉分支信息

可以使用

```cmd
git merge --no-ff -m "merge with no-ff" dev
```

来禁止fastforward模式

在实际开发中，我们应该按照几个基本原则进行分支管理：

首先，`master`分支应该是非常稳定的，也就是仅用来发布新版本，平时不能在上面干活；

那在哪干活呢？干活都在`dev`分支上，也就是说，`dev`分支是不稳定的，到某个时候，比如1.0版本发布时，再把`dev`分支合并到`master`上，在`master`分支发布1.0版本；

你和你的小伙伴们每个人都在`dev`分支上干活，每个人都有自己的分支，时不时地往`dev`分支上合并就可以了。

所以，团队合作的分支看起来就像这样：

![1543239622704](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543239622704.png)

### Bug分支

stash 可以把当前工作现场储藏起来，等以后恢复现场继续工作

```cmd
git stash
git stash apply //git stash drop
git stash pop
git stash list
git stash apply stash@[0]//恢复指定的stash
```

###Feature分支

```cmd
git checkout -b feature-vulcan	
```

commit 后

```cmd
git checkout dev
```

这时如果需要删除未合并的branch 需要使用D参数

```cmd
git branch -D feature-vulcan
```

### 多人协作

远程仓库的默认名称是origin

可以本地创建新的分支，再push到远程仓库origin上

从远程库clone时，默认情况只能看到本地的master分支

```git
git branch
```

若要在dev上进行开发，就必须创建远程origin的dev分支到本地，于是他用这个命令创建本地dev分支

```cmd
git checkout -b dev origin/dev
```

若同时有人对相同文件做了修改，则需要pull最新的提交，然后本地合并，解决冲突，再推送

```
git pull
//失败，根据提示 需要设置本地分支与远程分支的链接
git branch --set-upstream-to=orgin/dev dev
```

然后

```cmd
git pull
```

然后根据提示解决冲突，然后再push

![1543561921184](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1543561921184.png)

可以通过

```cmd
git rebase
```

整理提交过的内容为一条直线

### 创建标签

```
//切换到要打tag的分支后
git tag v1.0
git show v1.0
git tag -a v0.1 -m "version 0.1 released" 1094adb //-a 指定标签名 -m 指明说明文字
git tag -d v0.1
git push origin v1.0
git push origin v1.0 --tags //一次性推送所有tag
//删除已推送到远程的tag
git tag -d v0.9
git push origin：refs/tags/v0.9
```

![1543563873646](d:\Users\pcmpc\Desktop\learngit\GIT使用.assets\1543563873646.png)

### 推送开源项目的更新

![1543564120439](d:\Users\pcmpc\Desktop\learngit\GIT使用.assets\1543564120439.png)

尝试通过pullrequest来提交对开源项目的更新

国内可使用码云来进行项目代码管理

### 自定义Git

```
git config --global color.ui true //让git显示颜色
git config --global alias.st status//设置status的别名为st
git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit"
```

可以在  .gitignore文件中把需要忽略的文件名填进去

然后将.gitignore推送到远程库

当添加某文件不成功时

可以通过

```cmd
git add -f "文件名" //强制添加
//或者查看.gitignore文件是否出现问题
git check-ignore -v "文件"
```

### 自己搭建Git服务器

需要准备一台Ubuntu或Debian这样运行linux的机器

```
sudo apt-get install git
sudo adduser git
选定一个目录作为git仓库，
```

#####收集所有需要登录的用户的公钥，即id_rsa.pub文件，并导入到/home/git/.ssh/authorized_key文件中，一行一个

##### 选定一个目录作为git仓库

比如/srv/sample.git

```cmd
sudo git init --bare sample.git//创建一个裸仓库，服务器上的仓库一般以.git作为结尾
sudo chown -R git：git sample.git //把owner改为git
```

##### 禁用shell登录

通过编辑/etc/passwd文件完成。找到类似下面的一行

```
git:x:1001:1001:,,,:/home/git:bin/bash
//改为
git:x:1001:1001:,,,:/home/git:/usr/bin/git-shell
```

##### 克隆远程仓库

```cmd
git clone git@server:/srv/sample.git
```

管理公钥，以及管理权限可以通过 Gitosis  Gitolite