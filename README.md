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
| --- | --- | --- |
| `success` | function(result) |  |
| `result.isMock` | bool | true если gps фейковое |
| `error` | function(err) |  |
| `err.error` | string |  |
| `err.errors` | string[] |  |

## EMPlugin.getMockPermissionApps(success, error)
| Свойство | Тип | Значение |
| --- | --- | --- |
| `success` | function(result) |  |
| `result` | Object[] |  |
| `result[].name` | string |  |
| `result[].error` | string |  |

## EMPlugin.saveImageToGallery(path, options)
Копирует файл в галерею
| Свойство | Тип | Значение |
| --- | --- | --- |
| `path` | string | Путь к файлу |
| `options` | Object |  |
| `options.success` | function() |  |
| `options.error` | function(string) |  |
| `options.filename` | string | Название файла в галерее |
| `options.description` | string | Описание файла в галерее |