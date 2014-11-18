VKTokenAutoRefresher
====================

This application was made to automatically update/receive token to access the vk.com social
network data objects

If on android browser you are already logged on vk.com, token will be received automatically,
otherwise you will be prompted to log in.

The first time you launch application, it will show settings screen where you will need to enter
the ID of your vk.com application and a link to which will be sent received token, you decide how
yiu will use this token.

Reference format http://url:port/path dont specify any additional parameters for this link, there
will be added additional parameters to querystring ?Token=%received_token%

====================

Это приложение было сделано для автоматического обновления/получения токена, для доступа к нужным
данным соц сети vk.com

Если на андроиде в браузере вы уже авторизовались на vk.com, токен будет получен автоматически,
  в противном случае вам будет предложено авторизоваться.

При первом запуске, приложение откроет экран настроек, где вам нужно будет ввести идентификатор
вашего приложения и ссылку, на которую будет отправляться полученный токен, как вы будете работать
с токеном после получения на серверной стороне, решать вам.

Формат ссылки http://url:port/path, не указывайте никаких дополнительных параметров, к этой ссылке
будет добавлен query string ?token=%received_token%