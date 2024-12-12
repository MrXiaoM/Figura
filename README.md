<h1 align="center"> Figura λ </h1>
<p align="center">
  <img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg">
  <img alt="forge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/forge_vector.svg">
  <img alt="quilt" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/quilt_vector.svg">
  <img alt="neoforge" height="56" src="https://cdn.jsdelivr.net/gh/Hyperbole-Devs/vectors@neoforge_badges/assets/cozy/supported/neoforge_vector.svg">
</p>

为适应国内 Minecraft 服务器生态的 [Figura](https://github.com/FiguraMC/Figura) 修改版，尽量解决 Figura 在非正版验证服务器上的缺点。

> 作者精力有限，仅维护 1.20.4 Fabric/Forge/NeoForged 版本。  
> 如有其它版本需求且不熟悉 Github 操作，可在 [Discussions](https://github.com/MrXiaoM/FiguraLambda/discussions) 请求合并变更。
> 总的来说，有人提出要求，我再合并变更到相应版本的分支。

## Lambda 版本主要做了什么

+ ~~修复一些问题~~
+ 玩家连接后端服务器后，下载自己的模型并应用，而不是使用本地最后使用的模型
+ 可以直接使用 `/figura load` 命令载入导出的 `.moon` 文件
+ 提高 `world tick` 代码指令数的各权限限制，以免部分模型因为无法加载脚本出现渲染问题
+ 可让后端控制玩家不可上传模型
+ Minecraft 服务器可发送 CustomPayload 包，让 Figura 重新尝试连接后端服务器
+ 更多内容即将来临

## Figura Lambda 生态软件

+ [FiguraLambda](https://github.com/MrXiaoM/FiguraLambda): 客户端Mod `<-- 你在这里`
+ [sculptor](https://github.com/MrXiaoM/sculptor): 第三方后端(fork)
+ [FiguraAuthProvider](https://github.com/MrXiaoM/FiguraAuthProvider): 服务端/代理端 玩家验证插件
+ FiguraModels: 服务端 模型管理插件 *开发中*
