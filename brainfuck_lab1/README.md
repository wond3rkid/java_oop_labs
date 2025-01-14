# Brainfuck Interpreter

###### **Short:**        240 byte compiler. Fun, with src. OS 2.0

###### **Uploader:**     umueller amiga physik unizh ch

###### **Type:**         dev/lang

###### **Architecture:** m68k-amigaos

## The brainfuck compiler knows the following instructions:

**Cmd Effect**

```                         
+    Increases element under pointer
-    Decrases element under pointer
>    Increases pointer                    
<    Decreases pointer                    
[    Starts loop, flag under pointer      
]    Indicates end of loop                
.    Outputs ASCII code under pointer     
,    Reads char and stores ASCII under ptr
```

_**Who can program anything useful with it? :)**_




## Технические требования

При сдаче продемонстрировать исполнение 2-3 нетривиальных программ (решение квадратного уравнения, вычисление числа Пи с
помощью ряда Лейбница и т. д.).

Для создания команд использовать фабрику объектов. Каждая команда является экземпляром соответствующего
класса-наследника общего интерфейса.

Фабрика объектов использует механизм Java Reflection для создания объектов. Фабрика конфигурируется в момент своего
создания файлом, в котором указано соответствие между именами команд и полными квалифицированными именами классов,
которые соответствуют каждой команде (например, ]=ru.nsu.ivanov.Brainfuck.commands.LoopEnd). После этого, при получении
первого запроса на создание объекта по имени ], фабрика пытается загрузить указанный класс при помощи метода
Class.forName() и в случае успешной загрузки, инстанцирует экземпляр этого класса при помощи Class.Constructor().

Класс каждой команды не должен “загружаться” более одного раза, т.е. загруженные экземпляры java.lang.Class требуется
кэшировать.  
Фабрика не должна зависеть от конкретных классов команд.

Конфигурационный файл для фабрики загружать с помощью ClassLoader.getResourceAsStream(). Для разбора файла удобно
использовать класс java.util.Properties.

Классы программы должны быть протестированы с помощью JUnit (либо другого удобного инструмента).  

Ход выполнения программы должен быть журналирован с помощью библиотеки log4j (либо другой удобной).  

Основные классы и методы должны быть тщательно документированы в формате javadoc.  

Сборка, запаковывание в jar-файл и запуск должны быть реализованы с помощью технологии Gradle.

## Функциональность к контрольному сроку:

1. Полностью готова фабрика объектов.  
2. Готова часть команд, обязательно включая команды условного перехода.  
3. Готов один нетривиальный пример.

**Полный набор команд, тестов, логирование и сборка должны быть полностью готовы к крайнему сроку.**


### some examples
**Hello world:**
```bash
++++++++++[>+++++++>++++++++++>+++<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.
```