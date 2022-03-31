# CFTmoney

Проект был создан согласно [заданию](https://drive.google.com/file/d/122MrDCIUBKL6DkZHnSjbYOYP_6_Oy84K/view)

![one](https://github.com/RustamPlanirovich/CFTmoney/blob/master/app/screen/1.png) ![two](https://github.com/RustamPlanirovich/CFTmoney/blob/master/app/screen/2.png) ![three](https://github.com/RustamPlanirovich/CFTmoney/blob/master/app/screen/3.png)

В проекте задействованы библиотеки:

Retrofit 2
Dagger Hilt
View Binding
Room

Согласно заданию:
Приложение загружает данные с указанного сайта

Загруженные данные хранятся во ViewModel. При повороте экрана и сворачивании приложения данные не перезагружаются
Данные можно перезагружать вручную посредством проведения списка сверху вниз, а также в автоматическом режиме, установив соответствующий флажок. Обновление происходит 1 раз в минуту. Состояние флажка сохраняется в preferences.
Предоставлена возможность конвертирования указанной суммы в рублях в выбранную валюту согласно формуле (рубль * номинал) / курс

