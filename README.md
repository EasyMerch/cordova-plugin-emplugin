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
Android only<br />
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

## EMTimeChangeTracker
Android only<br />
Отслеживает переводы времени пользователем. Время может переводиться автоматически, обычно не больше чем на минуту, поэтому важно проверять интервал.

### getTimeChanges(success, error)
Возвращает массив, в который записана информациям по переводам времени
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `success` | function(result) |  |
| `result` | [timeChangeInfo](#timeChangeInfo)[] |  |
| `error` | function(message) |  |

### clearTimeChanges(options)
Очищает массив с информацией по переводам времени
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `options` | Object |  |
| `options.success` | function() |  |
| `options.error` | function(message) |  |

### watch(success, options) -> int
Добавляет listener на изменение времени<br />
Возвращает id listener'а
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `success` | function(result) |  |
| `result` | [timeChangeInfo](#timeChangeInfo) |  |
| `options` | Object |  |
| `options.error` | function(message) |  |

### clearWatch(id)
Удаляет listener на изменение времени
| Аргумент | Тип | Значение |
| --- | --- | --- |
| `id` | int | id listener'а, который надо удалить |

### timeChangeInfo
| Свойство | Тип | Значение |
| --- | --- | --- |
| `timestamp` | int | Время на телефоне после того, как было переведено время, в миллисекундах |
| `timeChangeInterval` | int | На сколько перевели время, в миллисекундах. Информация о переводе может доходить чуть позже, поэтому `timeChangeInterval` может немного отличаться от фактического интервала |
| `status` | string | Статус из таблицы ниже |

#### Возможные статусы
| Код | Значение |
| --- | --- |
| `OK` | Время переведено |
| `UNKNOWN` | Время переведено, но не известно на сколько |
| `SUPPOSED` | Пользователь *предположительно* перевел время. В случае, если пользователь остановил приложение (force stop) и перезагрузил устройство, это будет ложное срабатывание |