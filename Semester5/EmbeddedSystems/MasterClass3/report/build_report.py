from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
from copy import deepcopy
import hashlib, json
from lxml import etree as E
from PIL import Image, ImageDraw, ImageFont

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'report'; ASSETS=OUT/'assets'
REF=ROOT.parent/'MasterClass2/report/Отчет_МК2_Вариант1.docx'
NS={'w':'http://schemas.openxmlformats.org/wordprocessingml/2006/main','r':'http://schemas.openxmlformats.org/officeDocument/2006/relationships'}
def q(s):return '{'+NS['w']+'}'+s
fontfile='/System/Library/Fonts/Supplemental/Arial.ttf'
boldfile='/System/Library/Fonts/Supplemental/Arial Bold.ttf'
def font(size,bold=False):return ImageFont.truetype(boldfile if bold else fontfile,size)
def text(draw,xy,s,size=25,bold=False):draw.multiline_text(xy,s,font=font(size,bold),fill='black',anchor='mm',align='center',spacing=4)
SOFTWARE='#D9EAF7'; HARDWARE='#FCE4CC'
def sequence(name,height,labels,hardware,arrows):
 im=Image.new('RGB',(1400,height),'white');d=ImageDraw.Draw(im)
 xs=[110+1180*i/(len(labels)-1) for i in range(len(labels))]
 for i,(x,label) in enumerate(zip(xs,labels)):
  d.rectangle((x-90,12,x+90,124),fill=HARDWARE if i in hardware else SOFTWARE,outline='black',width=2)
  text(d,(x,67),label,23,True)
  for y in range(135,height-80,18):d.line((x,y,x,y+9),fill='#909090',width=1)
 for y,a,b,label in arrows:
  x1,x2=xs[a],xs[b]
  if a==b:
   d.line((x1,y,x1+35,y,x1+35,y+22,x1,y+22),fill='black',width=2)
   d.polygon([(x1,y+22),(x1+12,y+16),(x1+12,y+28)],fill='black')
   d.multiline_text((x1-12 if a==len(xs)-1 else x1+45,y-12),label,font=font(23),fill='black',spacing=3,anchor='ra' if a==len(xs)-1 else 'la')
  else:
   d.line((x1,y,x2,y),fill='black',width=2)
   sign=1 if x2>x1 else -1
   d.polygon([(x2,y),(x2-sign*12,y-6),(x2-sign*12,y+6)],fill='black')
   lines=label.count('\n')+1; center=((x1+x2)/2,y-14-lines*12)
   if label.startswith('Очередь'):
    box=d.multiline_textbbox(center,label,font=font(23),anchor='mm',align='center',spacing=4)
    d.rectangle((box[0]-8,box[1]-4,box[2]+8,box[3]+4),fill=SOFTWARE)
   text(d,center,label,23)
 for x,color,label in [(110,SOFTWARE,'Программные сущности'),(780,HARDWARE,'Аппаратные сущности')]:
  d.rectangle((x,height-52,x+32,height-24),fill=color,outline='black')
  d.text((x+45,height-52),label,font=font(23),fill='black')
 im.save(ASSETS/name)
sequence('sequence_init.png',1080,['main\nфункция','TIM1 TIM2\nI2C1 GPIO\nSysTick','FreeRTOS\nОС','OLED\nдисплей'],{1,3},[
 (190,0,1,'HAL_Init и HSI 16 МГц'),(280,0,1,'GPIO I2C1 и Buzzer_Init\nTIM2_CH1 PA5'),
 (370,0,3,'oled_Init: команды и пустой кадр'),(470,0,2,'Очереди commands frames sounds\nМьютекс i2c_mutex'),
 (570,0,2,'Задачи Input Game Display Sound'),(660,0,2,'osKernelStart'),
 (755,2,2,'Game: длина 3\nрежим PAUSE'),(865,2,3,'Display: начальный кадр\n8 страниц по 128 байт'),
 (970,1,2,'SysTick: такт ОС 1 мс')])
sequence('sequence_loop.png',1500,['Input\nзадача\nприоритет 4','i2c_mutex\nмьютекс','I2C1\nPCA9538\nOLED','Game\nзадача\nприоритет 3','Display\nзадача\nприоритет 2','Sound\nзадача\nприоритет 2','TIM2_CH1\nPA5\nзуммер'],{2,6},[
 (190,0,1,'Take'),(280,0,2,'PCA9538: опрос 4 строк'),(370,0,1,'Give'),
 (470,0,3,'Очередь commands: нажатие'),(570,3,3,'Команда\nи шаг'),
 (670,3,4,'Очередь frames:\nкопия игры'),(790,3,5,'Очередь sounds: событие'),
 (900,5,6,'PWM: частота ноты'),(1000,4,1,'Take'),
 (1100,4,2,'OLED: страница 128 байт'),(1200,4,1,'Give'),
 (1320,4,4,'Задержка 1 мс')])
for name,title in [('start','Начальное состояние'),('growth','Рост после поедания еды'),('pause','Пауза во время игры'),('loss','Столкновение со стеной')]:
 src=Image.open(ASSETS/(name+'.pgm')).convert('RGB').resize((768,384),Image.Resampling.NEAREST)
 im=Image.new('RGB',(850,510),'white');d=ImageDraw.Draw(im)
 text(d,(425,28),title,27,True);im.paste(src,(41,65));d.rectangle((40,64,809,450),outline='#777777',width=2)
 text(d,(425,480),'Программная модель OLED 128 x 64',21)
 im.save(ASSETS/(name+'.png'))

texts={
6:'Отчет по мастер классу 3',
7:'Программирование микроконтроллеров\nс операционной системой FreeRTOS',
8:'Игра Змейка на стенде SDK1.1M',
30:'Цель работы — освоить программирование микроконтроллера с операционной системой FreeRTOS и реализовать игру «Змейка» на учебном стенде SDK1.1M с матричной клавиатурой и OLED дисплеем. За основу взят проект SDK_FreeRTOS с использованием STM32 HAL и CMSIS RTOS v1.',
31:'По варианту 1 змейка перемещается по полю, растет при поедании еды и набирает очки. Клавиатура задает направление, паузу и перезапуск. Состояние игры выводится на OLED; столкновение со стеной или телом завершает игру.',
33:'Настроить обмен с PCA9538 и OLED по общей шине I2C1.',
34:'Организовать опрос клавиатуры 4 × 3 с подавлением дребезга и выделением новых нажатий.',
35:'Разделить ввод, игру, отрисовку и звук на четыре задачи FreeRTOS.',
36:'Использовать три очереди и один общий мьютекс для шины I2C1.',
37:'Реализовать рост, счет, ускорение, паузу, перезапуск и обработку столкновений.',
39:'В HAL адреса заданы со сдвигом: 0xE2 для PCA9538 и 0x78 для OLED. Настройка строк повторяет SDK_Keyboard. SysTick обеспечивает такты FreeRTOS, а TIM1 — миллисекундный счетчик HAL. TIM2_CH1 на PA5 управляет зуммером по примеру SDK_Buzzer; USART1 сохранен.',
42:'Голубым показаны программные сущности, оранжевым — аппаратные. Input, Game, Display и Sound — задачи FreeRTOS; i2c_mutex — мьютекс. commands, frames и sounds — очереди, подписанные на стрелках обмена. main — функция, FreeRTOS — ОС.',
44:'Рисунок 1 — Инициализация периферии и запуск задач FreeRTOS',
45:'До старта ОС main настраивает периферию и OLED. После osKernelStart задача Game создает змейку и включает паузу, а Display показывает начальное состояние. TIM1 и SysTick используют раздельные временные базы.',
49:'Рисунок 2 — Опрос клавиатуры передача команд и обновление OLED',
50:'Очередь commands передает команды из Input в Game; frames — копию игры из Game в Display; sounds — события из Game в Sound. Input и Display используют i2c_mutex для обмена по I2C1. Задача Sound управляет аппаратным TIM2_CH1 на PA5.',
54:'После сброса HAL_Init настраивает HAL и временную базу TIM1. SystemClock_Config выбирает HSI 16 МГц. Затем настраиваются GPIO, USART1 и I2C1 на PB8 и PB9 со скоростью 100 кГц. Драйвер OLED передает команды инициализации и очищает экран.',
55:'MX_FREERTOS_Init создает четыре задачи, три очереди и один мьютекс. Input имеет приоритет AboveNormal, Game — Normal, Display и Sound — BelowNormal. SysTick задает такт ОС 1 мс. Input и Game используют vTaskDelayUntil с периодом 10 мс; задачи ожидания не занимают процессор.',
57:'Задача Input вызывает keyboard_read и последовательно проверяет четыре строки. Перед каждой строкой записываются POLARITY_INVERSION, OUTPUT_PORT и CONFIG, как в SDK_Keyboard. Маски 0xFE, 0xFD, 0xFB и 0xF7 выбирают строку, переводимую в низкий уровень; остальные строки остаются входами.',
58:'Из INPUT_PORT выделяются биты 4–6. Строка и столбец определяют один из 12 битов маски клавиатуры. Три одинаковых чтения подтверждают изменение. В очередь commands отправляется событие нового нажатия; несколько одновременно нажатых клавиш игнорируются. Ошибка I2C не считается отпусканием.',
59:'commands содержит восемь элементов uint8_t; при заполнении новая команда отбрасывается без блокировки Input. Game — единственный владелец состояния игры. frames содержит одну копию SnakeGame: xQueueOverwrite оставляет последний кадр, поэтому отрисовка не задерживает движение. sounds хранит восемь событий uint8_t; при заполнении звук пропускается. Стеки Input, Game, Display и Sound — 256, 256, 512 и 256 слов.',
60:'Состояния игры и размещение еды',
61:'На старте длина змейки равна трем клеткам и включена пауза. Клавиша 5 запускает или приостанавливает движение; * начинает новую игру на паузе. Направления 2, 4, 6 и 8 означают вверх, влево, вправо и вниз. Разворот на 180° запрещен; за один шаг принимается только один поворот.',
62:'Поле имеет размер 30 × 12 клеток. Еда появляется только во внутренних свободных клетках, с отступом в одну клетку от краев. За еду начисляется очко и добавляется сегмент. Интервал движения уменьшается с 240 до 90 мс. Столкновение означает проигрыш; заполнение всех внутренних клеток — победу.',
64:'3 Передача данных между задачами и периферией',
66:'Клавиатура и OLED используют блокирующие HAL_I2C_Mem_Read и HAL_I2C_Mem_Write на общей I2C1. Мьютекс с наследованием приоритета исключает одновременный обмен. Input удерживает его на время сканирования, Display — на время одной страницы. DMA и прерывания клавиатуры не используются.',
67:'Display ожидает frames и формирует буфер 1024 байта. В верхней строке шрифтом 7 × 10 показаны состояние и счет; ниже — рамка, змейка и еда. Буфер передается восемью страницами по 128 байт. Между страницами мьютекс освобождается и выполняется задержка FreeRTOS 1 мс. Буфер принадлежит только Display. Sound — единственный владелец TIM2: старт, пауза, перезапуск, еда, проигрыш и победа имеют разные сигналы. Частота PWM задает высоту тона, заполнение — 50%; нулевая частота отключает звук. vTaskDelay задает длительность нот, освобождая процессор.',
70:'Подключить питание стенда и USB DBG. Импортировать SDK_Snake в STM32CubeIDE и собрать Debug либо выполнить ./lab.sh build.',
71:'Выполнить ./lab.sh flash: скрипт соберет, загрузит, проверит и запустит прошивку через FTDI/JTAG с SDK11M.cfg.',
72:'Проверить появление PAUSE 0, рамки, змейки и еды. Раскладка сверху вниз: 1 2 3 / 4 5 6 / 7 8 9 / * 0 #.',
73:'Проверить звуки старта, паузы, еды и проигрыша. Нажать 5 для старта. Управлять клавишами 2, 4, 6 и 8, отпуская каждую перед следующим нажатием. Повторное 5 включает паузу.',
74:'Удерживать направление не требуется. После проигрыша направления и 5 не действуют: нажать * для новой игры, затем 5 для старта. В правую сторону при старте змейка движется автоматически; 4 не разворачивает ее назад. Еда отмечена крестиком, голова — квадратом с темным центром.',
77:'Изображения получены программной моделью OLED: выполнены snake.c, текущая отрисовка, oled.c и fonts.c; передача I2C заменена захватом буфера. Для демонстрации роста задано положение еды перед головой. Модель не воспроизводит физические сигналы и планирование задач.',
79:'Рисунок 3 — Начальная змейка и ожидание нажатия 5',
80:'После запуска показаны PAUSE 0 и змейка длиной три клетки. Движение начинается только после 5. Такая же начальная пауза устанавливается после перезапуска клавишей *.',
82:'Рисунок 4 — Увеличение длины и счета после поедания еды',
83:'При переходе головы на еду счет становится равным 1, длина увеличивается до четырех клеток. Следующая еда выбирается внутри поля, вне тела змейки и вне крайних строк и столбцов.',
85:'4 Пауза и завершение игры',
87:'Рисунок 5 — Пауза с сохранением игрового поля и счета',
88:'Нажатие 5 переводит игру в PAUSE. Координаты сегментов, положение еды и счет сохраняются. Повторное нажатие продолжает движение; пауза не блокирует опрос клавиатуры.',
90:'Рисунок 6 — Проигрыш после столкновения со стеной',
91:'При выходе следующего шага за границу устанавливается LOST *. Змейка остается в последнем допустимом положении. Нажатие * сбрасывает длину и счет и создает новую игру на паузе; после 5 можно играть снова.',
94:'Контрольные примеры приведены в таблице. Каждая клавиша означает отдельное нажатие и отпускание. Сценарии роста и столкновений проверены автоматическими тестами игровой логики.',
95:'Сборка ARM GCC завершена: text 24496 байт, data 112 байт, bss 20256 байт. Тесты прошли с контролем памяти и неопределенного поведения. Проверено сопоставление всех 12 клавиш и обработка ошибок I2C. Управление подтверждено на стенде ранее; новый звук требует проверки после загрузки. Кадры получены программной моделью.',
96:'5 Ссылка на репозиторий',
97:'https://github.com/rdydygin/ITMO/tree/main/Semester5/EmbeddedSystems/MasterClass3',
99:'Реализована «Змейка» на FreeRTOS. Ввод, игровая логика, вывод и звук выполняются четырьмя задачами. Очереди передают команды и копии состояния, мьютекс защищает I2C1. Игра поддерживает рост, счет, ускорение, паузу и перезапуск. Еда размещается внутри поля; столкновения обрабатываются без выхода за границы массива.'
}
# Patch only editable package parts. Preserve real styles, tables and cover formatting.
with ZipFile(REF) as z:
 parts={n:z.read(n) for n in z.namelist()}
root=E.fromstring(parts['word/document.xml']);body=root.find('w:body',NS);ps=body.findall('w:p',NS)
def replace_text(p,t):
 old=p.findall('.//w:t',NS)
 if not old:raise ValueError('No text slot')
 # Preserve paragraph and first run formatting, but replace line-break-aware content.
 first=old[0];run=first.getparent()
 for node in p.findall('.//w:t',NS)+p.findall('.//w:br',NS):
  if node.tag==q('br') and node.get(q('type'))=='page':continue
  node.getparent().remove(node)
 for i,line in enumerate(t.split('\n')):
  if i:run.append(E.Element(q('br')))
  n=E.SubElement(run,q('t'));n.text=line;n.set('{http://www.w3.org/XML/1998/namespace}space','preserve')
for i,t in texts.items():replace_text(ps[i],t)
tables=body.findall('w:tbl',NS)
rows0=[['Устройство или сигнал','Подключение и назначение'],['Микроконтроллер STM32F407','Cortex M4; HSI 16 МГц'],['I2C1 SCL и SDA','PB8 и PB9; скорость 100 кГц'],['PCA9538','Адрес 0x71; клавиатура 4 × 3'],['OLED 128 × 64','Адрес 0x3C; вывод поля и счета'],['SysTick TIM1 TIM2','Такты ОС; HAL; зуммер PA5'],['Программатор SDK1.1M','FTDI/JTAG; загрузка прошивки']]
rows1=[['Клавиша','Действие'],['2 / 4 / 6 / 8','Вверх / влево / вправо / вниз'],['5','Старт или переключение паузы'],['*','Новая игра на паузе']]
rows2=[['Клавиши или действие','Ожидаемый результат'],['Запуск или *','PAUSE 0; длина 3'],['5 при начальной паузе','Движение вправо'],['2 / 6 / 8 / 4','Изменение направления'],['4 при движении вправо','Разворот не выполняется'],['Два поворота до шага','Принимается только первый'],['Поедание еды','Счет +1; длина +1'],['5 во время движения','Поле и счет сохраняются'],['Повторное 5 на паузе','Движение продолжается'],['Стена или тело','LOST *; движение прекращается'],['Внутренние клетки заняты','WIN *; еда на краях не появляется']]
for table,rows in zip(tables,[rows0,rows1,rows2]):
 for tr,values in zip(table.findall('w:tr',NS),rows):
  for cell,value in zip(tr.findall('w:tc',NS),values):replace_text(cell.find('w:p',NS),value)
parts['word/document.xml']=E.tostring(root,encoding='UTF-8',xml_declaration=True,standalone=True)
rels=E.fromstring(parts['word/_rels/document.xml.rels'])
for rel in rels:
 if rel.get('Id')=='rId20':rel.set('Target','https://github.com/rdydygin/ITMO/tree/main/Semester5/EmbeddedSystems/MasterClass3')
parts['word/_rels/document.xml.rels']=E.tostring(rels,encoding='UTF-8',xml_declaration=True,standalone=True)
for i,file in enumerate(['sequence_init.png','sequence_loop.png','start.png','growth.png','pause.png','loss.png'],6):parts[f'word/media/image{i}.png']=(ASSETS/file).read_bytes()
core=E.fromstring(parts['docProps/core.xml'])
for tag,t in [('title','Игра Змейка на стенде SDK1.1M'),('subject','Мастер класс 3 Вариант 1')]:
 n=core.find('{http://purl.org/dc/elements/1.1/}'+tag)
 if n is not None:n.text=t
parts['docProps/core.xml']=E.tostring(core,encoding='UTF-8',xml_declaration=True,standalone=True)
final=OUT/'Отчет_МК3_Вариант1.docx'
with ZipFile(final,'w',ZIP_DEFLATED) as z:
 for n,data in parts.items():z.writestr(n,data)
with ZipFile(REF) as z:
 changed=[n for n in z.namelist() if z.read(n)!=parts[n]]
allowed={'word/document.xml','word/_rels/document.xml.rels','docProps/core.xml',*[f'word/media/image{i}.png' for i in range(6,12)]}
assert set(changed)<=allowed,changed
(OUT/'package_verification.json').write_text(json.dumps({'reference_sha256':hashlib.sha256(REF.read_bytes()).hexdigest(),'changed_parts':changed,'preserve_only_parts':'unchanged'},ensure_ascii=False,indent=2))
print(final)
