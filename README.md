# Restaurant

## Работу выполнил 
Мухин Дмитрий БПИ228

## Какие шаблоны проектирования применяются?
### Builder
- Используется при создании объекта UserEntity. При увеличении количества полей в этом классе, не придется использовать грамоздкий конструктор.
### Singleton
- Используется для хранения информации о запущенных потоках и о текущей сессии. Нужен потому, что необходимо, чтобы класс, хранящий потоки, и текущаяя сессия были представлены единственными объектами. 
### Chain of Responsibility
- Используется при валидации данных пользователя. Нужно, чтобы избежать нагромождения обработчиков полей при входе и регистрации.
### State


## Как пользоваться приложением?
- В самом начале работы программы происходит попытка загрузить данные о фильмах, сеансах и польователях. Если какой-то файл не удается загрузить, пользователь получает соответсвующее сообщение.
## Регистрация
- Далее пользователь попадает в меню регистрации.
- На этом этапе он должен войти в аккаунт или зарегестрироваться.
- После успешного входа пользователь попадает в главное меню.
## Главное меню
- После пользователь видит главное меню, из него он может попасть в меню работы с фильмами, или с сеансами, или с местами в зале.
- Он выбирает соответствующую опцию и переходит в другое меню.
## Меню работы с заказами

## Пример работы программы
```
Registration menu. Enter
"log in" If you already have an account
"sign up" In order to make an account
"exit" In order to finish program
log in
Enter login and password in format: login; password
MainUser; 123456
Main menu. Enter
"meals" To go to meals menu
"orders" To go to orders menu
"statistics" To go to statistics menu
"log out" In order to log out
"exit" In order to finish program
orders
Orders menu. Enter
"show all" In order to see the list of orders
"make" In order to make an order
"add" In order to add a meal to an order
"remove" In order to remove a meal from an order
"show" In order to see an order details
"cancel" In order to cancel an order
"start cooking" In order to start cooking an order
"pay" In order to pay for an order
"duration" In order to see the duration of an order
"stop cooking" In order to stop cooking an order
"exit" In order to exit to main menu
show all
OrderEntity(id=0, userId=1, duration=2m, meals={0=5}, totalPrice=1750, startedOn=2024-03-12T20:27:17.508103600, state=Ready)
OrderEntity(id=1, userId=1, duration=1m, meals={1=2}, totalPrice=600, startedOn=2024-03-12T20:28:10.911217500, state=Paid)
OrderEntity(id=4, userId=1, duration=3m, meals={0=24}, totalPrice=8400, startedOn=2024-03-12T21:40:15.073155200, state=Ready)
OrderEntity(id=5, userId=1, duration=1m, meals={1=1}, totalPrice=300, startedOn=null, state=Created)
Orders menu. Enter
"show all" In order to see the list of orders
"make" In order to make an order
"add" In order to add a meal to an order
"remove" In order to remove a meal from an order
"show" In order to see an order details
"cancel" In order to cancel an order
"start cooking" In order to start cooking an order
"pay" In order to pay for an order
"duration" In order to see the duration of an order
"stop cooking" In order to stop cooking an order
"exit" In order to exit to main menu
start cooking
Enter orderId
5
The order with id 5 is being prepared
Orders menu. Enter
"show all" In order to see the list of orders
"make" In order to make an order
"add" In order to add a meal to an order
"remove" In order to remove a meal from an order
"show" In order to see an order details
"cancel" In order to cancel an order
"start cooking" In order to start cooking an order
"pay" In order to pay for an order
"duration" In order to see the duration of an order
"stop cooking" In order to stop cooking an order
"exit" In order to exit to main menu
duration
Enter orderId
5
Time left: 44.068468200s
Orders menu. Enter
"show all" In order to see the list of orders
"make" In order to make an order
"add" In order to add a meal to an order
"remove" In order to remove a meal from an order
"show" In order to see an order details
"cancel" In order to cancel an order
"start cooking" In order to start cooking an order
"pay" In order to pay for an order
"duration" In order to see the duration of an order
"stop cooking" In order to stop cooking an order
"exit" In order to exit to main menu
duration
Enter orderId
5
The order is prepared
Orders menu. Enter
"show all" In order to see the list of orders
"make" In order to make an order
"add" In order to add a meal to an order
"remove" In order to remove a meal from an order
"show" In order to see an order details
"cancel" In order to cancel an order
"start cooking" In order to start cooking an order
"pay" In order to pay for an order
"duration" In order to see the duration of an order
"stop cooking" In order to stop cooking an order
"exit" In order to exit to main menu
stop cooking
Enter orderId
5
The order with id 5 is already prepared
Orders menu. Enter
"show all" In order to see the list of orders
"make" In order to make an order
"add" In order to add a meal to an order
"remove" In order to remove a meal from an order
"show" In order to see an order details
"cancel" In order to cancel an order
"start cooking" In order to start cooking an order
"pay" In order to pay for an order
"duration" In order to see the duration of an order
"stop cooking" In order to stop cooking an order
"exit" In order to exit to main menu
exit
Main menu. Enter
"meals" To go to meals menu
"orders" To go to orders menu
"statistics" To go to statistics menu
"log out" In order to log out
"exit" In order to finish program
exit
Program is finished
```
