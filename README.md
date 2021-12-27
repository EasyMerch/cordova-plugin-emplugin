# cordova-plugin-emplugin

## EMPlugin.device
| Свойство | Тип | Значение |
| --- | --- | --- |
| `aviable` | bool | Информация по устройству получена |
| `isVirtual` | bool | Приложение запущено на эмуляторе |
| `serial` | string | Серийный номер устройства (пока не работает) |
| `info` | string | Различная информация по устройству |

## EMPlugin.locationIsMock(success, error)
Проверка на фейк gps
| Свойство | Тип | Значение |
| --- | --- |
| `success` | function(result) |  |
| `result.isMock` | bool | true если gps фейковое |
| `error` | function(err) |  |
| `err.error` | string |  |
| `err.errors` | string[] |  |

## EMPlugin.getMockPermissionApps(success, error)
Не работает на Android 11+
| Свойство | Тип | Значение |
| `success` | function(result) |  |
| `result` | Object[] |  |
| `result[].name` | string |  |
| `result[].error` | string |  |