# 个人学习资料知识库管理系统需求规格说明书

## 1. Project Summary

- Product type: 私有/半私有 Web 学习资料知识库管理系统
- One-sentence goal: 为个人与少量被授权成员提供一个可管理、可检索、可在线学习、可追踪状态的统一学习资料中心
- Target users: 系统管理员（你本人）与少量被授权普通成员
- Primary scenario: 管理员维护学习资料和分类权限，普通用户按授权范围检索、打开、继续学习资料
- Platforms: Web，优先桌面端，兼容移动端基础浏览与播放
- Success definition:
  - 管理员可在 5 分钟内完成一个新分类或新专辑的创建，并录入一批新资料
  - 普通用户可在 30 秒内通过分类导航或搜索打开目标资料
  - 用户可在资料列表中快速识别未读资料，并一键回到最近访问的资料
  - 权限控制可以确保普通用户仅能看到被授权分类下的内容

## 2. Product Goals And Non-Goals

### Goals

- 建立统一的学习资料管理入口，覆盖文档、音频、视频、HTML 等多种资料类型
- 将资料文件本体保留在第三方对象存储中，由系统管理元数据、权限、访问和学习状态
- 提供站内预览/播放体验，减少跳出第三方页面的学习中断
- 提供分类、专辑、资料三级组织结构，提升资料的可管理性和可检索性
- 提供按标题、作者、分类、专辑等维度的快速检索能力
- 记录资料级别的学习状态，包括已读/未读、完成状态、最后访问时间
- 提供基于分类的用户授权机制

### Non-Goals

- 不做公开内容平台，不面向陌生用户开放注册
- 不做社交能力，例如点赞、评论、关注、动态流
- 不做 AI 摘要、AI 问答、智能推荐
- 不做多端原生 App
- 不做复杂在线编辑、协同编辑、批注系统
- 不做文档内部精细进度跟踪，例如 PDF 页码、视频秒级断点续播、HTML 锚点恢复

## 3. Target Users And Roles

### Primary Users

- 系统管理员
  - 负责资料体系建设、分类维护、专辑维护、资料录入、用户创建、分类授权
- 授权普通用户
  - 负责查看被授权资料、搜索资料、在线学习、标记已读、继续上次学习

### Secondary Users

- 暂无

### Roles And Permissions

- Role: 管理员
  - Can:
    - 创建、编辑、停用用户账号
    - 创建、编辑、删除分类
    - 创建、编辑、删除专辑
    - 创建、编辑、删除资料元数据
    - 选择资料来源存储商并维护 object key
    - 发起批量导入和对象存储导入
    - 配置普通用户可见的分类
    - 查看所有资料和所有用户状态
  - Cannot:
    - 无业务限制

- Role: 普通用户
  - Can:
    - 登录系统
    - 查看被授权分类下的专辑和资料
    - 搜索被授权范围内的资料
    - 在线预览或播放资料
    - 标记资料已读/未读
    - 查看并继续最近访问资料
  - Cannot:
    - 创建、编辑、删除分类、专辑、资料
    - 查看未授权分类内容
    - 管理其他用户
    - 修改全局系统配置

## 4. Scope

### In Scope For V1

- 管理员账号登录
- 普通用户账号登录
- 管理员创建账号或邀请成员
- 分类管理
- 专辑管理
- 资料管理
- 三种资料导入方式：
  - 手工录入
  - 批量导入资料元数据
  - 从阿里云 OSS / 七牛云读取对象列表导入
- 资料列表浏览
- 资料搜索
- 站内访问能力：
  - PDF 站内预览
  - 音频站内播放
  - 视频站内播放
  - HTML 站内渲染
- 资料学习状态管理：
  - 已读/未读
  - 完成状态
  - 最后访问时间
- 最近访问资料快速继续
- 基于分类的权限控制
- 管理后台与普通用户使用界面

### Later / Optional

- 资料标签体系增强
- 收藏夹或稍后再看
- 数据统计看板
- 操作审计详情页
- DOC/DOCX 在线预览增强
- 更细粒度授权，例如按专辑或单资料授权
- 资料封面、简介、标签、排序、备注等扩展字段的高级管理

### Explicitly Out Of Scope

- AI 摘要/问答
- 多端 App
- 开放注册
- 社区互动能力
- 文档内部细粒度学习断点恢复

## 5. Core User Flows

### Flow 1: 管理员创建分类、专辑并录入资料

- Entry point: 管理后台首页或资料管理页
- Steps:
  1. 管理员登录后台
  2. 创建或选择一个分类
  3. 在分类下创建一个专辑
  4. 通过手工录入、批量导入或对象存储导入添加资料
  5. 为资料填写或补齐基础字段
  6. 保存并发布资料
- Success outcome:
  - 资料出现在对应分类与专辑下，可被授权用户搜索和访问
- Failure or exception paths:
  - 对象存储连接失败，系统提示失败原因并支持重试
  - object key 不存在或不可访问，资料保存失败或标记为异常
  - 必填字段缺失时不可提交

### Flow 2: 普通用户搜索并学习资料

- Entry point: 首页、分类页、搜索页、最近访问区
- Steps:
  1. 用户登录系统
  2. 浏览被授权分类或直接使用搜索
  3. 打开目标资料
  4. 系统在站内预览或播放资料
  5. 用户学习后可标记已读/未读或完成
- Success outcome:
  - 用户成功打开资料并完成学习状态更新
- Failure or exception paths:
  - 无权限时提示无访问权限，不展示资料实际内容
  - 存储资源不可用时提示暂时无法访问，并保留列表可见性
  - 文件类型不支持站内预览时显示降级策略

### Flow 3: 用户继续最近学习资料

- Entry point: 首页“继续学习”模块或资料列表快捷入口
- Steps:
  1. 用户登录系统
  2. 系统展示最近访问且未完成的资料列表
  3. 用户点击某条资料继续学习
  4. 系统直接打开该资料详情或阅读页
- Success outcome:
  - 用户无需重复搜索即可继续上次资料
- Failure or exception paths:
  - 资料已被移除或失去权限时，系统提示不可访问并从继续学习列表移除

### Flow 4: 管理员配置分类权限

- Entry point: 用户管理页
- Steps:
  1. 管理员选择一个用户
  2. 为该用户勾选可访问分类
  3. 保存权限配置
  4. 用户重新登录或刷新权限后生效
- Success outcome:
  - 用户只能看到被授权分类下的内容
- Failure or exception paths:
  - 分类被删除时，系统自动移除无效授权
  - 保存失败时保留原权限并提示原因

## 6. Functional Requirements

### FR-1 用户认证与账号管理

- Objective: 提供仅管理员可创建/邀请的账号体系，并支持管理员与普通用户登录
- User value: 保证系统私有性与访问可控
- Trigger:
  - 管理员进入用户管理页
  - 用户访问登录页
- Inputs:
  - 用户名
  - 密码
  - 昵称
  - 角色
  - 可访问分类集合
- Outputs:
  - 新建用户账号
  - 登录会话
  - 权限信息
- Business rules:
  - 不开放公开注册
  - 仅管理员可创建普通用户或其他管理员账号
  - 用户登录后仅获取自身角色和授权分类数据
  - 用户被停用后不可登录
- Permissions:
  - 管理员可创建、编辑、停用账号
  - 普通用户仅可登录和使用自身账号
- States:
  - 未激活
  - 正常
  - 停用
- Edge cases:
  - 重复用户名不可创建
  - 首次登录是否强制改密为 Assumption
  - 邀请链接有效期为 Assumption

Acceptance criteria:

- [ ] 系统不提供公开注册入口
- [ ] 管理员可以创建普通用户账号并设置初始密码或邀请信息
- [ ] 停用用户后，该用户无法再次登录
- [ ] 登录成功后，前端只能获取当前用户可见的权限范围

### FR-2 分类管理

- Objective: 提供资料的一级组织结构，并作为权限控制的最小授权单元
- User value: 让资料按主题或领域有序沉淀
- Trigger: 管理员进入分类管理页
- Inputs:
  - 分类名称
  - 分类编码
  - 分类描述
  - 显示排序
  - 启用状态
- Outputs:
  - 分类记录
  - 分类列表
- Business rules:
  - 分类是权限控制最小粒度
  - 一个分类下可以有多个专辑
  - 分类可启用/停用
  - 停用分类默认对普通用户隐藏
- Permissions:
  - 仅管理员可管理分类
- States:
  - 启用
  - 停用
- Edge cases:
  - 分类名称重复处理规则为 Assumption：同级不可重名
  - 分类下存在专辑或资料时，删除需受限制

Acceptance criteria:

- [ ] 管理员可以新增、编辑、停用分类
- [ ] 普通用户只能看到被授权且启用的分类
- [ ] 删除分类时，系统必须阻止删除仍有关联专辑或资料的分类，或要求先迁移数据

### FR-3 专辑管理

- Objective: 在分类下构建资料集合，便于按课程、主题、系列组织内容
- User value: 提升资料聚合和连续学习体验
- Trigger: 管理员进入专辑管理页或某分类详情页
- Inputs:
  - 所属分类
  - 专辑名称
  - 专辑描述
  - 显示排序
  - 启用状态
- Outputs:
  - 专辑记录
  - 分类下专辑列表
- Business rules:
  - 专辑必须隶属于一个分类
  - 一个专辑下可以有多个资料
  - 专辑不能跨分类
- Permissions:
  - 仅管理员可管理专辑
- States:
  - 启用
  - 停用
- Edge cases:
  - 专辑删除前需处理所含资料

Acceptance criteria:

- [ ] 管理员可在指定分类下创建专辑
- [ ] 专辑列表按所属分类正确展示
- [ ] 普通用户仅可见授权分类下已启用专辑

### FR-4 资料管理

- Objective: 管理资料元数据及其与存储对象的映射关系
- User value: 将分散在对象存储中的学习资料统一纳入可管理知识库
- Trigger:
  - 管理员手工新建资料
  - 管理员批量导入资料
  - 管理员从对象存储导入资料
- Inputs:
  - 标题
  - 作者
  - 文件类型
  - 存储提供商
  - 存储路径/object key
  - 所属分类
  - 所属专辑
  - 发布时间
  - 入库时间
  - 其他候选扩展字段：
    - 副标题
    - 简介
    - 封面
    - 文件大小
    - 标签
    - 排序值
    - 是否展示
    - 备注
- Outputs:
  - 资料记录
  - 资料列表
  - 可访问的站内预览/播放链接
- Business rules:
  - 资料必须属于一个分类
  - 资料应优先属于一个专辑；是否允许无专辑资料为 Assumption：允许
  - 存储提供商至少支持 OSS、七牛云
  - 系统需根据资料类型决定预览方式
  - object key 必须可映射到真实文件
- Permissions:
  - 仅管理员可创建、编辑、删除资料
  - 普通用户仅可查看有权限资料
- States:
  - 草稿
  - 已发布
  - 已停用
  - 异常
- Edge cases:
  - object key 无效
  - 文件不存在
  - 文件类型与真实资源不一致
  - 存储服务临时不可用

Acceptance criteria:

- [ ] 管理员可以手工创建资料并绑定 object key
- [ ] 管理员可以编辑资料基础元数据
- [ ] 普通用户不会看到未发布、停用或未授权资料
- [ ] 当资源不可访问时，资料详情页给出明确错误提示

### FR-5 三种导入能力

- Objective: 降低资料录入成本，兼顾灵活性与效率
- User value: 管理员可按资料来源选择最合适的导入方式
- Trigger:
  - 进入资料导入页
- Inputs:
  - 手工录入表单
  - 批量导入文件
  - OSS / 七牛云连接配置或已配置连接
- Outputs:
  - 导入结果
  - 成功/失败清单
- Business rules:
  - 导入优先级：
    - P0: 手工录入
    - P1: 批量导入元数据
    - P1: 从对象存储读取对象列表导入
  - 批量导入模板字段需与资料模型对齐
  - 导入过程需校验必填字段和重复记录
- Permissions:
  - 仅管理员可导入
- States:
  - 待导入
  - 导入中
  - 部分成功
  - 导入成功
  - 导入失败
- Edge cases:
  - 批量数据缺字段
  - 重复 object key
  - 存储桶无法连接
  - 导入过程中断

Acceptance criteria:

- [ ] 系统支持手工录入资料
- [ ] 系统支持通过模板批量导入资料元数据
- [ ] 系统支持从 OSS 或七牛云对象列表中选择并导入资料
- [ ] 每次导入后系统展示成功条数、失败条数和失败原因

### FR-6 资料浏览与搜索

- Objective: 让用户能快速定位到被授权资料
- User value: 减少翻找成本，提高学习效率
- Trigger:
  - 用户打开首页、分类页、专辑页、搜索页
- Inputs:
  - 关键词
  - 分类筛选
  - 专辑筛选
  - 作者筛选
  - 资料类型筛选
  - 已读/未读筛选
- Outputs:
  - 资料列表
  - 搜索结果
- Business rules:
  - 搜索范围仅限用户有权限访问的资料
  - 至少支持标题、作者搜索
  - “内容搜索”若指文档正文检索，则为 Open Question；v1 默认不承诺全文索引
  - 列表需支持按最近入库、发布时间、最近访问排序
- Permissions:
  - 仅可返回当前用户有权访问的数据
- States:
  - 空结果
  - 正常结果
  - 搜索失败
- Edge cases:
  - 搜索词为空时展示推荐列表或全部列表
  - 分类无资料时显示空状态

Acceptance criteria:

- [ ] 用户可以通过关键词搜索资料标题和作者
- [ ] 用户可以按分类、专辑、文件类型、已读状态筛选资料
- [ ] 无权限资料不会出现在搜索结果中
- [ ] 空结果页明确提示并提供返回浏览路径

### FR-7 站内预览与播放

- Objective: 提供尽量原生的站内学习体验
- User value: 用户无需跳转第三方平台即可完成阅读或播放
- Trigger: 用户打开资料详情或点击资料卡片
- Inputs:
  - 资料 ID
  - 用户访问凭证
- Outputs:
  - PDF 预览页
  - 音频播放器
  - 视频播放器
  - HTML 渲染页
- Business rules:
  - PDF 必须支持站内预览
  - 音频必须支持站内播放
  - 视频必须支持站内播放
  - HTML 必须支持站内渲染
  - 对象存储访问必须通过受控访问方式生成访问地址
  - 对于不支持的文件类型，系统需给出降级策略
- Permissions:
  - 仅授权用户可生成访问地址
- States:
  - 可预览
  - 加载中
  - 资源异常
  - 不支持预览
- Edge cases:
  - 访问链接过期
  - 跨域配置错误
  - 浏览器不支持某编码格式
  - HTML 内容包含外链资源或潜在风险脚本

Acceptance criteria:

- [ ] PDF 在站内页面可直接预览
- [ ] 音频和视频在站内页面可直接播放
- [ ] HTML 在站内页面可渲染
- [ ] 资源加载失败时，页面展示明确错误状态与重试入口

### FR-8 学习状态与继续学习

- Objective: 记录资料级学习状态并支持快速回到最近学习资料
- User value: 用户能快速识别未读内容并减少重复查找成本
- Trigger:
  - 用户打开资料
  - 用户主动标记已读/未读
- Inputs:
  - 用户 ID
  - 资料 ID
  - 已读/未读
  - 完成状态
  - 最后访问时间
- Outputs:
  - 用户资料学习状态记录
  - 最近访问列表
  - 未完成资料列表
- Business rules:
  - 仅记录资料级别状态，不记录文档内部页码、时间点或章节锚点
  - “最后访问位置”在 v1 中等价为“最后访问到的资料项”
  - 资料列表可提供“继续学习”快捷入口
  - 用户可手动修改已读/未读或完成状态
- Permissions:
  - 用户仅可修改自己的学习状态
  - 管理员可查看所有用户学习状态为 Optional
- States:
  - 未读
  - 已读
  - 学习中
  - 已完成
- Edge cases:
  - 资料被删除或失去权限后需自动清理或忽略状态记录

Acceptance criteria:

- [ ] 用户打开资料后，系统记录最近访问时间
- [ ] 用户可以手动标记资料已读或未读
- [ ] 首页或资料页可展示最近访问且未完成的资料列表
- [ ] 用户可通过“继续学习”入口再次打开最近访问资料

### FR-9 分类权限控制

- Objective: 以分类为单位控制用户可见范围
- User value: 管理员能简洁而明确地控制内容访问范围
- Trigger:
  - 管理员设置用户权限
  - 普通用户浏览分类、专辑、资料
- Inputs:
  - 用户 ID
  - 可访问分类 ID 集合
- Outputs:
  - 权限配置结果
  - 基于权限过滤后的分类/专辑/资料视图
- Business rules:
  - 权限粒度固定为分类级
  - 用户被授权某分类后，可访问该分类下所有启用专辑和已发布资料
  - 未被授权分类不应在 UI 中显示
  - 后端 API 必须再次校验权限，不能仅依赖前端隐藏
- Permissions:
  - 仅管理员可配置分类权限
- States:
  - 已授权
  - 未授权
- Edge cases:
  - 分类停用后，所有普通用户自动不可见
  - 用户被取消授权后，历史访问链接失效

Acceptance criteria:

- [ ] 管理员可为任一普通用户勾选多个可访问分类
- [ ] 普通用户登录后仅能看到被授权分类下的数据
- [ ] 用户直接访问未授权资料链接时，后端返回权限错误

## 7. Screens And UI/UX Requirements

### Visual Direction

- Brand or mood keywords: Apple 风格、简约、透明、清晰、克制、精致、以内容为中心
- Reference products: Apple 官网与 Apple 系统应用的视觉语言，结合知识库后台与在线阅读器布局
- Typography direction:
  - 中文优先
  - 字体风格要求中性、现代、清晰
  - 标题层级明确，留白充足，避免过重字重和拥挤排版
- Color direction:
  - 整体以浅色主题为主
  - 使用低饱和中性色和少量柔和强调色
  - 背景允许使用轻微层次变化、毛玻璃或半透明容器
  - 避免高饱和撞色、厚重阴影和杂乱装饰
- Component style:
  - 组件视觉参考 Apple Human Interface 风格
  - 使用较大的圆角、轻量边框、半透明卡片、细腻阴影
  - 列表、筛选、表单、导航区分明确，但避免传统后台过重边框感
  - 内容区域优先，控制工具栏和筛选栏视觉噪音
- Motion and interaction tone:
  - 轻量、顺滑、克制
  - 页面切换、悬停、弹窗、抽屉可使用短时淡入、位移、模糊过渡
  - 禁止夸张动画和明显打扰阅读的动效
- Implementation guidance:
  - 前端应抽取统一设计变量，包括颜色、圆角、阴影、模糊强度、间距、排版层级
  - 重点页面应保持一致的玻璃拟态轻质感，但不得牺牲可读性与性能
  - 管理后台虽然偏工具型，也应保持简洁、精致，而不是传统厚重企业后台风格

### Screen Inventory

#### Screen A: 登录页

- Purpose: 用户输入凭证并进入系统
- Key information:
  - 系统名称
  - 用户名
  - 密码
- Primary actions:
  - 登录
- Secondary actions:
  - 忘记密码为 Optional
- Empty state:
  - 不适用
- Loading state:
  - 登录按钮 loading
- Error state:
  - 用户名或密码错误
  - 账号停用
- Responsive behavior:
  - 桌面端居中登录卡片
  - 移动端纵向排列表单

#### Screen B: 普通用户首页

- Purpose: 提供分类入口、继续学习、最近资料和搜索入口
- Key information:
  - 被授权分类
  - 最近访问资料
  - 未完成资料
  - 搜索框
- Primary actions:
  - 继续学习
  - 进入分类
  - 搜索资料
- Secondary actions:
  - 按状态筛选
- Empty state:
  - 无授权分类
  - 暂无学习记录
- Loading state:
  - 骨架屏或列表 loading
- Error state:
  - 资料加载失败
- Responsive behavior:
  - 桌面端两栏或三栏
  - 移动端单列卡片

#### Screen C: 分类页/专辑页/资料列表页

- Purpose: 按层级浏览授权资料
- Key information:
  - 分类信息
  - 专辑列表
  - 资料列表
  - 筛选与排序
- Primary actions:
  - 打开资料
  - 切换专辑
  - 筛选资料
- Secondary actions:
  - 标记已读/未读
- Empty state:
  - 暂无资料
- Loading state:
  - 列表 loading
- Error state:
  - 获取资料失败
- Responsive behavior:
  - 桌面端左侧分类/专辑导航，右侧列表
  - 移动端切换为顶部筛选 + 列表

#### Screen D: 搜索页

- Purpose: 通过关键词和筛选条件定位资料
- Key information:
  - 关键词
  - 分类筛选
  - 专辑筛选
  - 作者筛选
  - 文件类型筛选
  - 已读状态筛选
- Primary actions:
  - 执行搜索
  - 打开资料
- Secondary actions:
  - 清空筛选
- Empty state:
  - 无匹配结果
- Loading state:
  - 搜索中
- Error state:
  - 搜索失败
- Responsive behavior:
  - 桌面端筛选栏固定
  - 移动端筛选抽屉

#### Screen E: 资料详情/学习页

- Purpose: 站内预览或播放资料，并显示学习状态操作
- Key information:
  - 标题
  - 作者
  - 文件类型
  - 所属分类/专辑
  - 已读/完成状态
  - 最近访问时间
- Primary actions:
  - 在线预览/播放
  - 标记已读/未读
  - 标记完成/未完成
- Secondary actions:
  - 返回列表
- Empty state:
  - 资料不可用
- Loading state:
  - 预览容器 loading
- Error state:
  - 资源获取失败
  - 无权限
  - 当前类型不支持预览
- Responsive behavior:
  - 桌面端大阅读区/播放区
  - 移动端自适应播放器和滚动容器

#### Screen F: 管理后台首页

- Purpose: 管理员快速进入分类、专辑、资料、用户管理
- Key information:
  - 管理模块导航
  - 资料数量概览
  - 用户数量概览
  - 最近导入记录为 Optional
- Primary actions:
  - 进入资料管理
  - 进入分类管理
  - 进入用户管理
- Secondary actions:
  - 查看最近异常记录为 Optional
- Empty state:
  - 首次使用引导
- Loading state:
  - 卡片 loading
- Error state:
  - 后台摘要加载失败
- Responsive behavior:
  - 桌面端左侧菜单 + 内容区
  - 移动端折叠菜单

#### Screen G: 资料管理页

- Purpose: 管理资料、导入资料、编辑资料
- Key information:
  - 资料表格
  - 分类/专辑筛选
  - 导入入口
  - 状态信息
- Primary actions:
  - 新建资料
  - 编辑资料
  - 删除资料
  - 导入资料
- Secondary actions:
  - 批量操作为 Optional
- Empty state:
  - 暂无资料
- Loading state:
  - 表格 loading
- Error state:
  - 获取列表失败
- Responsive behavior:
  - 优先桌面端表格布局
  - 移动端仅保留基础浏览与简单编辑

#### Screen H: 用户管理页

- Purpose: 创建用户并配置分类权限
- Key information:
  - 用户列表
  - 角色
  - 状态
  - 分类授权
- Primary actions:
  - 创建用户
  - 编辑用户
  - 停用用户
  - 配置授权分类
- Secondary actions:
  - 重置密码为 Optional
- Empty state:
  - 暂无用户
- Loading state:
  - 表格 loading
- Error state:
  - 保存权限失败
- Responsive behavior:
  - 优先桌面端表格 + 抽屉/弹窗编辑

### Accessibility Expectations

- Keyboard support:
  - 登录、搜索、列表操作、弹窗表单需支持键盘基本操作
- Contrast expectations:
  - 正文与交互元素需满足清晰可读，避免低对比浅灰文字
- Screen-reader considerations:
  - 表单字段、按钮、搜索输入、播放控件需有明确语义标签
- Localization or content-length resilience:
  - 默认中文
  - 长标题、长分类名、长作者名需可截断或换行

## 8. Data Model

### Entity 1: User

- Description: 系统登录用户
- Required fields:
  - id
  - username
  - password_hash
  - display_name
  - role
  - status
  - created_at
- Optional fields:
  - last_login_at
  - invited_at
  - remark
- Relationships:
  - 与分类权限表为多对多
  - 与学习状态表为一对多
- Validation rules:
  - username 唯一
  - role 仅允许 `ADMIN` / `USER`

### Entity 2: Category

- Description: 资料一级分类，亦为权限控制单元
- Required fields:
  - id
  - name
  - code
  - status
  - sort_order
  - created_at
- Optional fields:
  - description
- Relationships:
  - 一对多关联专辑
  - 一对多关联资料
- Validation rules:
  - code 唯一

### Entity 3: Album

- Description: 分类下的资料专辑
- Required fields:
  - id
  - category_id
  - name
  - status
  - sort_order
  - created_at
- Optional fields:
  - description
- Relationships:
  - 多对一关联分类
  - 一对多关联资料
- Validation rules:
  - 同一分类下专辑名建议唯一

### Entity 4: Material

- Description: 学习资料元数据记录
- Required fields:
  - id
  - title
  - author
  - file_type
  - storage_provider
  - object_key
  - category_id
  - created_at
  - ingest_time
  - publish_status
- Optional fields:
  - album_id
  - publish_time
  - subtitle
  - summary
  - cover_url
  - file_size
  - tags
  - sort_order
  - remark
- Relationships:
  - 多对一关联分类
  - 多对一关联专辑
  - 一对多关联学习状态
- Validation rules:
  - title 非空
  - author 非空
  - file_type 必须在受支持枚举内
  - storage_provider 必须为 `OSS` 或 `QINIU`
  - object_key 非空

### Entity 5: UserCategoryPermission

- Description: 用户与分类的授权关系
- Required fields:
  - id
  - user_id
  - category_id
  - created_at
- Optional fields:
  - created_by
- Relationships:
  - 多对一关联用户
  - 多对一关联分类
- Validation rules:
  - user_id + category_id 唯一

### Entity 6: LearningProgress

- Description: 用户对资料的学习状态记录
- Required fields:
  - id
  - user_id
  - material_id
  - read_status
  - completion_status
  - last_accessed_at
  - updated_at
- Optional fields:
  - first_accessed_at
- Relationships:
  - 多对一关联用户
  - 多对一关联资料
- Validation rules:
  - user_id + material_id 唯一

### Entity 7: ImportTask

- Description: 批量导入或对象存储导入任务
- Required fields:
  - id
  - import_type
  - status
  - total_count
  - success_count
  - fail_count
  - created_by
  - created_at
- Optional fields:
  - error_summary
  - source_provider
  - source_bucket
  - source_prefix
- Relationships:
  - 多对一关联管理员用户
- Validation rules:
  - import_type 必须在预设枚举内

## 9. Integrations And External Services

### Integration 1: 阿里云 OSS

- Purpose: 存储学习资料文件，并供系统拉取对象列表和生成可访问资源地址
- Direction: 双向，读取对象信息 + 受控访问文件
- Data exchanged:
  - bucket
  - object key
  - 文件元信息
  - 预签名访问地址或等效受控访问地址
- Authentication:
  - AccessKey / Secret 或具备最小权限的 RAM 配置
- Failure handling:
  - 连接失败时提示配置异常
  - 文件不存在时资料标记异常
- Retry or fallback behavior:
  - 支持手动重试连接或重试导入

### Integration 2: 七牛云

- Purpose: 存储学习资料文件，并供系统拉取对象列表和生成可访问资源地址
- Direction: 双向，读取对象信息 + 受控访问文件
- Data exchanged:
  - bucket
  - object key
  - 文件元信息
  - 私有访问地址
- Authentication:
  - AccessKey / SecretKey 与空间配置
- Failure handling:
  - 连接失败、空间不存在、对象不存在时给出明确错误信息
- Retry or fallback behavior:
  - 支持手动重试连接或重试导入

## 10. Technical Preferences And Constraints

- Preferred frontend stack:
  - 用户未限制
  - Assumption: 为了快速交付管理后台与阅读型界面，前端默认采用 `Vue 3 + TypeScript + Vite + Element Plus`
- Preferred backend stack:
  - `Java`
  - Assumption: `Spring Boot 3.x + Spring Security + JWT/Session + MyBatis-Plus`
- Database preference:
  - `MySQL 8.0`
- Deployment target:
  - 云服务器
  - `Nginx + Java 服务 + MySQL`
- Auth approach:
  - 账号密码登录
  - 不开放自注册
  - 管理员创建或邀请
- Files, storage, or CDN needs:
  - 文件本体存储于 OSS / 七牛云
  - 应支持私有资源受控访问
  - 若视频播放性能需要，可结合 CDN 为 Optional
- Forbidden or undesired technologies:
  - 无强制禁用项
- Existing systems to reuse:
  - 无

## 11. Non-Functional Requirements

- Performance:
  - 普通资料列表页在 1,000 条以内数据量下，常规筛选响应时间应小于 2 秒
  - 关键词搜索在常规数据量下响应时间应小于 2 秒
  - 站内预览页应在 3 秒内开始加载可视内容，受第三方存储网络影响除外
- Security:
  - 所有资料访问接口必须做后端权限校验
  - 对象存储访问应使用受控地址，不暴露长期有效公开链接
  - 密码必须加密存储
  - 管理接口需进行角色校验
- Reliability:
  - 存储服务短时异常不应导致系统整体不可用
  - 导入失败需可重试，且不影响已成功数据
- Observability:
  - 需记录登录失败、导入失败、资源访问失败等关键错误日志
  - 需记录关键后台操作日志为 Should have
- Privacy or compliance:
  - 系统仅面向私有授权成员，不做公开搜索引擎暴露
- Accessibility:
  - 满足基础可读性与表单可操作性要求
- SEO, if relevant:
  - 不相关
- Browser or device support:
  - 支持近两年主流 Chrome、Edge、Safari
  - 移动端支持基础浏览、搜索、预览和播放

## 12. Analytics, Admin, And Operations

- Admin features:
  - 用户管理
  - 分类管理
  - 专辑管理
  - 资料管理
  - 导入任务查看
- Audit logs:
  - v1 建议至少记录：
    - 用户创建
    - 用户停用
    - 分类权限修改
    - 资料创建/编辑/删除
    - 导入任务执行
- Metrics to track:
  - 资料总量
  - 分类总量
  - 用户总量
  - 最近访问次数
  - 导入成功/失败次数
- Error monitoring:
  - 后端异常日志
  - 导入失败日志
  - 资源访问失败日志
- Content moderation or approval:
  - 不需要

## 13. Acceptance And Testing

### Feature-Level Acceptance

- 管理员能够完成从创建分类到导入资料再到授权用户访问的完整闭环
- 普通用户只能在授权分类范围内浏览、搜索、预览、播放资料
- 用户能看到自己的最近访问资料并继续学习
- 站内预览/播放覆盖 PDF、音频、视频、HTML 四类资料
- 导入能力能覆盖手工录入、批量导入、对象存储导入三类入口

### Suggested Test Coverage

- Unit:
  - 权限判断逻辑
  - 资料状态机
  - 学习状态更新逻辑
  - 导入数据校验逻辑
- Integration:
  - 登录鉴权流程
  - 用户分类授权生效流程
  - OSS / 七牛云对象列表读取
  - 受控访问地址生成
- End-to-end:
  - 管理员创建分类、专辑、资料并授权用户
  - 普通用户登录、搜索、打开资料、标记已读
  - 导入失败后查看错误并重试

## 14. Assumptions

- Assumption: 前端默认使用 `Vue 3 + TypeScript + Vite + Element Plus`
- Assumption: 后端默认使用 `Spring Boot 3.x`
- Assumption: 认证方式默认采用账号密码登录和 JWT 或服务端 Session，具体方案由实现阶段决定
- Assumption: 管理员创建用户时可直接设置初始密码，邀请链接机制可作为增强方案
- Assumption: 资料可以不属于专辑，但必须属于分类
- Assumption: v1 的搜索默认覆盖标题、作者、分类、专辑等结构化字段，不承诺全文内容索引
- Assumption: 对象存储访问采用私有桶 + 受控短时访问链接方案
- Assumption: 对于 DOC/DOCX 等未明确要求的格式，v1 可先采用下载或外部打开的降级策略

## 15. Open Questions

- Open Question: “快速查找内容”是否要求对 PDF / HTML / 文档正文做全文检索。如果需要，则需补充索引方案与导入解析链路
- Open Question: 管理员创建账号时，是直接分配初始密码，还是同时支持邮箱邀请链接
- Open Question: 是否需要资料删除回收站或软删除机制
- Open Question: 是否需要管理员查看普通用户的学习完成情况报表
- Open Question: HTML 站内渲染时是否需要做额外内容净化、外链拦截和脚本隔离策略
