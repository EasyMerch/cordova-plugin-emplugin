# cordova-plugin-emplugin

## EMPlugin

### device
| Свойство | Тип | Значение |
| --- | --- | --- |
| `aviable` | bool | Информация по устройству получена |
| `isVirtual` | bool | Приложение запущено на эмуляторе |
| `serial` | string | Серийный номер устройства (пока не работает) |
| `info` | string | Различная информация по устройству |

### saveImageToGallery(path, options)
Копирует файл в галерею
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `path` | string | Путь к файлу |
| `options` | Object |  |
| `options.success` | function() |  |
| `options.error` | function(string) |  |
| `options.filename` | string | Название файла в галерее (Android only) |
| `options.description` | string | Описание файла в галерее (Android only) |

### locationIsMock(success, error)
Android only
Проверка на фейк gps
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `success` | function(result) |  |
| `result.isMock` | bool | true если gps фейковое |
| `error` | function(err) |  |
| `err.error` | string |  |
| `err.errors` | string[] |  |

### getMockPermissionApps(success, error)
Android only
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `success` | function(result) |  |
| `result` | Object[] |  |
| `result[].name` | string |  |
| `result[].error` | string |  |

## EMTimeChangeCheker
Android only

### getTimeChanges(success, error)
Возвращает массив, в который записана информациям по переводам времени
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `success` | function(result) |  |
| `result` | Object[] |  |
| `result[].timestamp` | int | Время на телефоне после того, как было переведено время, в миллисекундах |
| `result[].timeChangeDifference` | int | На сколько перевели время, в миллисекундах |
| `result[].status` | String | `OK` - Можно верить<br />`UNKNOWN` - `timeChangeDifference` не известно<br />`SUPPOSED` - пользователь *предположительно* перевел время. В случае, если пользователь остановил приложение (force stop) и перезагрузил устройство, это будет ложное срабатывание) |
| `error` | function(message) |  |

### clearTimeChanges(options)
Очищает массив с информацией по переводам времени
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `options` | Object |  |
| `options.success` | function() |  |
| `options.error` | function(message) |  |

### watch(success, options) -> int
Добавляет listener на изменение времени
Возвращает id listener'а
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `success` | function() |  |
| `options` | Object |  |
| `options.error` | function(message) |  |

### clearWatch(id)
Удаляет listener на изменение времени
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `id` | int | id listener'а, который надо удалить |