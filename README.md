# Music Dealer Bot 
Цель: разработать такого чат-бота, который был бы способен предоставлять любую музыку(и вообще любое аудио из видео) пользователю, исходя из его запроса, для дальнейшего прослушивания или скачивания для проигрывания в режиме оффлайн. 

Преимуществом данного бота по сравнению с другими схожими является тот факт, что для скачивания не потребуется самостоятельно добытая ссылка на видео, поскольку его можно найти через поиск в самом боте, который задействует для этого Youtube API. Таким образом, для осуществления скачивания не будет необходимости использовать что-то, помимо самого бота.  

Таким образом, бот становится ответственным за обработку запроса пользователя, запроса в музыкальную библиотеку, выдачу результатов в виде нового сообщения в виде списка из 5 кнопок и, после выбора пользователем определённого варианта, ему остаётся сделать запрос на скачивание и, в конце концов, отослать терпеливому пользователю долгожданный аудио-файл.

Будучи написанным на языке Java, всего он задействует 4 файла – TelegramBot, ответственный за большую часть всей логики, Buttons – для конструирования списка кнопок выбора песен, YouTube – для запроса в библиотеку музыки и Console – для непосредственного скачивания файла на компьютер, а также вспомогательную утилиту yt-dlp. Предварительно был создан, настроен и запущен в работу бот с помощью BotFather, включая получение API-ключа от телеграма. 

Работу бота можно разбить на следующие этапы:
1)	Получение от пользователя команды /start, после чего бот сообщает о том, что он готов к работе.
  
2) Далее пользователь вводит свой запрос – будь-то название песни, исполнителя, альбома, видео – чего угодно, далее бот делает http-запрос на платформу YouTube (благодаря API-ключу) для поиска подходящего под запрос материала, вычленяет из него название, идентификатор, составляет ссылку (её нужно конструировать самостоятельно) для каждого из 5 вариантов, а затем присылает результат в виде списка кнопок, на которых указаны названия того или иного произведения.
  
3)	Затем пользователь, нажав на одну из кнопок, делает свой выбор и тем самым бот начинает процесс скачивания соответствующего аудио(всегда максимально возможного качества) с помощью специально сконструированного запроса в утилиту yt-dlp, предварительно оповестив пользователя об этом(“Downloading the song… it might take a while”). По завершении скачивания, оповещает о начале следующего этапа(“Uploading…”) и начинает загрузку только-только скачанного аудио-файла непосредственно в чат к пользователю. В примере ниже была нажата кнопка №2.
   
4)	Готово. Бот успешно отправил пользователю аудио-файл, который второй теперь может скачать и слушать даже без доступа в интернет, или сделать очередной запрос.

Пример работы:

![image](https://github.com/svy4toyboriy/MusicDealer/assets/135538976/0ca3da40-7103-4088-b4ba-a8df0ee41a63)
![image](https://github.com/svy4toyboriy/MusicDealer/assets/135538976/8a9758ff-b465-4be7-a481-a0bc49cacba2)
![image](https://github.com/svy4toyboriy/MusicDealer/assets/135538976/ca244a34-4e82-4dd6-a0a3-0c07b3db6dd0)
![image](https://github.com/svy4toyboriy/MusicDealer/assets/135538976/a2a480f8-2686-427b-be5e-e62006211a88)

Также важно отметить, что
1)	 Пользователь вправе выбрать и несколько вариантов из предложенных в кнопках, не делая ещё один запрос, т.е. он может использовать один и тот же список кнопок бесконечное число раз. 
2)	Пользователь может внезапно использовать команду /start без нарушения в работе бота. 
3)	Не выбирать ни одну из кнопок и сделать очередной запрос также без каких-либо проблем.

