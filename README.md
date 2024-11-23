# SusGame - Frontend

## App config

App config can be changed by editing the
**[`AppConfig.kt`](app/src/main/java/edu/agh/susgame/front/config/AppConfig.kt) file**

### Local (Mock) setup

Set the following in the
**[`AppConfig.kt`](app/src/main/java/edu/agh/susgame/front/config/AppConfig.kt) file**:
```
override val providers = ProviderType.MockLocal
```
The remaining configuration will be ignored

### Server setup
1. Run the server ([**SusGameBackend** repository](https://github.com/Nepommuck/SusGameBackend)) in the same network
2. Get your public IPv4 address. On Linux / macOS it can be achieved by:
    ```
    ❯ ip addr | grep "inet "
    inet 127.0.0.1/8
    inet 192.168.0.100/24 brd 192.168.0.255
    ```
   where `192.168.0.100` is what interests us
3. Update the **[`AppConfig.kt`](app/src/main/java/edu/agh/susgame/front/config/AppConfig.kt) file**:
    ```
    override val providers = ProviderType.Web

    override val webConfig = AppConfig.WebConfig(
        // ...
        defaultIpAddress = "192.168.0.15",
        // ...
    ```
    Setting the `defaultIpAddress` is optional, but will make it unnecessary to provide the IP every
    this the app is run.

## Working with DTO
- In order to modify DTO, commit to [**SusGameDTO** repository](https://github.com/Nepommuck/SusGameDTO)
- In order to update DTO to newest version, run [**`update-DTO.sh`**](./scripts/update-DTO.sh) script
- In order to update DTO to given **SusGameDTO** repository branch, run [**`update-DTO.sh`**](./scripts/update-DTO.sh) script with branch name as an argument
