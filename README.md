## App config

App config can be changed by editing the **Config file**: `app/src/main/java/edu/agh/susgame/front/Config.kt`

### Local (Mock) setup

Set the following in the **Config file** (`Config.kt`):
```
override val providers = ProviderType.MockLocal
```
The remaining configuration will be ignored

### Server setup
1. Run the server (`SusGameBackend` repository) in the same network
2. Get your public IPv4 address. On linux it can be achieved by:
    ```
    ❯ ip addr | grep "global dynamic"
    inet 192.168.0.15/24 brd 192.168.0.255 scope global dynamic noprefixroute wlp0s20f3
    ```
   where `192.168.0.15` is what interests us
3. Update the **Config file** (`Config.kt`):
    ```
    override val providers = ProviderType.Web

    override val webConfig = AppConfig.WebConfig(
        // ...
        domain = "192.168.0.15",
        // ...
    ```
4. Paste your IPv4 address into `app/src/main/res/xml/network_security_config.xml`:
    ```
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.0.15</domain>
    </domain-config>
    ```
