# cordova-plugin-emplugin

## EMPlugin.device
| Свойство | Тип | Значение |
| --- | --- | --- |
| `aviable` | bool | Информация по устройству получена |
| `isVirtual` | bool | Приложение запущено на эмуляторе |
| `serial` | string | Серийный номер устройства (пока не работает) |
| `info` | string | Различная информация по устройству |

## EMPlugin.locationIsMock(success, error)
| Свойство | Тип |
| --- | --- |
| `success` | function(result) |
| `result.isMock` | bool |
| `error` | function(err) |
| `err.error` | string |
| `err.errors` | string[] |