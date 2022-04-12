## 开发调试

### 组件的开发和调试

把 project 的 `build.gradle` 中的 `debugComponents` 属性改为 true 后同步，即可单独运行自己负责的模块。

### 集成调试

各功能组件开发得差不多后，可把 project 的 `build.gradle` 中的 `debugComponents` 属性改为 false，对所有功能进行整体的调试。

## 组件之间的交互

原则上是需要与其它组件或模块交互才增加路由，模块内的功能逻辑可以正常获取对象、使用  `startActivity()` 等进行开发，不然全加路由太复杂了。

### 添加依赖和初始化

添加需要用到的组件依赖：

```groovy
dependencies {
    implementation project(path: ':component-common') // 必须
    runtimeOnly project(path: ':component-user') // 可选
    runtimeOnly project(path: ':component-device') // 可选
    runtimeOnly project(path: ':component-mine') // 可选
    runtimeOnly project(path: ':component-message') // 可选
    ...
}
```

`Application` 继承 `ComponentApp` ，可以选择是否开启登录拦截。

```kotlin
class App : ComponentApp(needLogin = true) {
  ...
}
```

### 跳转组件的 Activity

在 Activity 增加 `@Route` 注解修饰，传入 path 参数。

```kotlin
@Route(path = ActivityPaths.MESSAGE_LIST)
class MessageListActivity : BaseActivity<ActivityMessageListBinding>() {
  ...
}
```

path 需要在公共模块的 `ActivityPaths` 常量类里定义，方便后续的调用。

```kotlin
object ActivityPaths {
  ...
  const val MESSAGE_LIST = "$GROUP_MESSAGE/message_list"
}
```

然后就可以通过路由跳转到该页面了。

```kotlin
startRouterActivity(ActivityPaths.MESSAGE_LIST)
startRouterActivityForResult(ActivityPaths.MESSAGE_LIST, RequestCodes.xxx)
```

如果跳转的页面可能需要登录，建议改用 `startRouterActivityCheckLogin()` 方法，开启登录拦截后，没登录的话会先跳到登录页面，否则正常跳转。

```kotlin
startRouterActivityCheckLogin(ActivityPaths.MAIN)
```

如果需要跳转后关闭之前的页面，不能直接加 `finish()`，否则转场动画会很奇怪。用了路由跳转想要正常的关闭之前页面，下面的方式：

```kotlin
startRouterActivity(ActivityPaths.MAIN) { finish() }
```

### 获取组件的 Fragment

在 Fragment 增加 `@Route` 注解修饰，传入 path 参数。

```kotlin
@Route(path = FragmentPaths.DEVICE_LIST)
class DeviceListFragment : BaseFragment<FragmentDeviceListBinding>() {
  ...
}
```

path 需要在公共模块的 `FragmentPaths` 常量类里定义，方便后续的调用。

```kotlin
object FragmentPaths {
  ...
  const val DEVICE_LIST = "$GROUP_DEVICE/device_list"
}
```

然后就可以通过 `by routerFragments()` 方法使用 Kotlin 委托获取 Fragment。

```kotlin
private val deviceListFragment by routerFragments(FragmentPaths.DEVICE_LIST)

override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  supportFragmentManager.commit {
    setReorderingAllowed(true)
    add(R.id.fragment_container_view, deviceListFragment)
  }
}
```

### 调用组件的功能或数据交互

定义一个接口继承 `IProvider` 接口类，建议命名为 xxxService。

```kotlin
interface MessageService : IProvider {
  val unreadCount: LiveData<Int>
}
```

在对应组件编写该接口的实现类，实现所需的方法。该类建议命名为 xxxServiceProvider。

```kotlin
@Route(path = "$GROUP_MESSAGE/service")
class MessageServiceProvider : MessageService {

  override val unreadCount get() = MessageRepository.unreadCount

  override fun init(context: Context) {}
}
```

然后就可以通过 `by routerServices()` 方法使用 Kotlin 委托获取接口的实例对象。

```kotlin
private val userService: UserService by routerServices()

override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  binding.btnLogout.setOnClickListener {
    userService.logout()
  }
}
```

通常一个接口只对应一个实现类，但是如果需要实现多方案，可以一对多。比如多种连接设备的方式，获取实例对象时需要传入对应的 path。

```kotlin
private val wifiConnectService: ConnectService by routerServices(ConnectService.Paths.WIFI_HOTSPOT)
private val gatewayConnectService: ConnectService by routerServices(ConnectService.Paths.GATEWAY)
```

1. git clone --recursive https://gitlab.apeman.com.cn/sz/app/android/pro/OsaioProject.git
  // 递归获取项目内模块代码，包括 submodule 内代码

2. git submodule foreach git checkout master
// 将所有 submodule 模块都切到master分支上

 3. git submodule foreach git pull origin master                                                                    // 拉取所有 submoudle master主分支代码
