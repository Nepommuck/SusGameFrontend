# SusGame - Frontend

## App config

App config can be changed by editing the **[`Config.kt`](app/src/main/java/edu/agh/susgame/front/Config.kt) file**

### Local (Mock) setup

Set the following in the **[`Config.kt`](app/src/main/java/edu/agh/susgame/front/Config.kt) file**:
```
override val providers = ProviderType.MockLocal
```
The remaining configuration will be ignored

### Server setup
1. Run the server ([**SusGameBackend** repository](https://github.com/Nepommuck/SusGameBackend)) in the same network
2. Get your public IPv4 address. On linux it can be achieved by:
    ```
    ❯ ip addr | grep "global dynamic"
    inet 192.168.0.15/24 brd 192.168.0.255 scope global dynamic noprefixroute wlp0s20f3
    ```
   where `192.168.0.15` is what interests us
3. Update the **[`Config.kt`](app/src/main/java/edu/agh/susgame/front/Config.kt) file**:
    ```
    override val providers = ProviderType.Web

    override val webConfig = AppConfig.WebConfig(
        // ...
        domain = "192.168.0.15",
        // ...
    ```

## Working with DTO
- In order to modify DTO, commit to [**SusGameDTO** repository](https://github.com/Nepommuck/SusGameDTO)
   
- In order to update DTO to newest version, run [**`update-DTO.sh`**](./scripts/update-DTO.sh) script
- 
- In order to update DTO to given **SusGameDTO** repository branch, run [**`update-DTO.sh`**](./scripts/update-DTO.sh) script with branch name as an argument
