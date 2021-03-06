/** Приминение EventHandler'а в Forge'е */

/**
 * Создатель оригинала туториала (eng): coolAlias
 * Перевел на русский: keelfy
 * Об ошибках, вопросах и предложениях о переводе писать сюда: http://vk.com/tehnyla
 */

/**
 *	P.S. (сделаю оговорку о переводе, чтобы все понимали что за шины и обработчики событий)
 *		Шина Событий 		= Event Bus
 *		Обработчик Событий  = Event Handler
 *		Событие 			= Event
 *		Слушатель Событий   = Метод с событием в параметре и с аннотацией @ForgeSubscribe (1.6 и ранее) или @SubcribeEvent (1.7+)
 */
 
/*
	Если вы хотите изменить любую стандартную реакцию Minecraft'а на какое либо действие Minecraft, то приминяйте EventHandler.
	В нем есть События Игрока (Player Events), События Живых Существ (Living Events), События Предметов (Item Events), 
	События Мира(World Events), Событие Генерации Мира (TerrainGenEvents), События Вагонетки (Minecart Events)... и многое другое
	Так что вы мжете сделать что-то невероятное, приминяя события
	Лично я предпочитаю применять EventHandler, вместо TickHandler'а. Так как многие вещи в TickHandler'е нужно писать самостоятельно,
	тогда как в EventHandler'e они уже сделаны.
	Этот туториал расскажет вам о:
	1. Построении и использовании Event Handler'а
	2. Расширенной информации про обработку событий
	3. Простых событиях и их возможных приминениях
*/

/**
 * Часть 1: Создание класса TutEventHandler
 */
/*
	ВАЖНО!!! Не называйте ваш обработчик событий 'EventHandler' - это название уже занято форджем.
	Также, ни в коем случае НЕ изменяйте классы форджа. Вам нужно создать новый класс для обработки событий.
*/

public class TutEventHandler
{
}

/**
 * Часть 2: Регистрация вашего EventHandler
 */
/*
	Регистрируйте ваш обработчик событий через EVENT_BUS в методах 'load' или 'postInit' вашего главного класса мода. 
	Эта часть является одинаковой для 1.6 и 1.7, дополнительная информация в конце туториала
 */

@EventHandler
public void load(FMLInitializationEvent event)
{
	// ВАЖНО: Убедитесь что вы выбрали верную шину событий !!! (смотрите ниже)

	// большая часть событий относится к Форджу:
	MinecraftForge.EVENT_BUS.register(new TutEventHandler());

	// но некоторые относятся к FML'ю и приходится их регистрировать по другому:
	FMLCommonHandler.instance().bus().register(new YourFMLEventHandler());
}

/*
	Обратите внимание: Регистрация нужной шины событий
	Вы сделали все в точности, как описанно, но все равно что-то идет не так? Каждое событие зарегистрированно в отдельной шине и если ваш обработчик зарегистрирован не на ту шину, то метод с ваши событием никогда не будет вызван. Большая часть событий зарегистрированна в MinecraftForge.EVENT_BUS, но есть и те события, что зарегистрированны в других шинах: 
	1. MinecraftForge.EVENT_BUS: Здесь большая часть событий.
	2. MinecraftForge.TERRAIN_GEN_BUS: Большая часть событий генерации здесь - заселение, декорирование..., но есть странное исключение для Pre и Post событий, они находятся в EVENT_BUS
	3. MinecraftForge.ORE_GEN_BUS: Очевидно - события генерации руд здесь
	4. FML Events: Эти события очень важны в 1.7, т.к. там существуют события TickEvent и KeyInputEvent, которые являются заменой для TickHandler'a и KeyHandler'a
	Очень важно регистрировать ваши события в правильные шины и использовать только те события, которые зарегистрированы в шине, иначе ваша система потерпит неудачу.
	Это было легко, но пока ничего не делайте. Переходим к части 3.
*/

/**
 * Часть 3: Добавление событий в ваш обработчик (как пример)
 */
 
/*
	Выбирите нужное вам событие из MinecraftForge, и добавьте его в свой EventHandler (обработчик событий) путем создания нового метод и установки события (Event'а) в качестве параметра метода.
	1.6: Используйте аннотацию "@ForgeSubscribe". Она вызывает событие в нужный момент.
	1.7: Используйте аннотацию "@SubscribeEvent". Она вызывает событие в нужный момент.
	Никогда, я повторю, никогда НЕ изменяйте классы форджа. Также, вам не требуется создание класса, унаследованного от класса события.
 */

// В вашем классе TutEventHandler - название метода не имеет значение
// Важно только название события в параметре метода (смотрите ниже пояснение некоторых вариантов событий)
@ForgeSubscribe
public void onLivingUpdateEvent(LivingUpdateEvent event)
{
	// Это событие имеет переменную Entity, получим к ней доступ:
	event.entity;

	// делаем что-нибудь с игроком каждый тик:
	if (event.entity instanceof EntityPlayer)
	{
		EntityPlayer player = (EntityPlayer) event.entity;
		ItemStack heldItem = player.getHeldItem();
		if (heldItem != null && heldItem.itemID == Item.arrow.itemID) {
			player.capabilities.allowFlying = true;
		}
		else {
			player.capabilities.allowFlying = player.capabilities.isCreativeMode ? true : false;
		}
	}
}

// Если вам станет интересно какие еще переменные содержит событие - напишите в методе 'event.'
// и Eclipse выведет вам все возможные переменные.
// Или просмотрите их с помощнью нажатия Ctrl'a по классу события.

/**
 * Часть 4: Использование событий в ваших классах
 */
/*
	События Форджа автоматически встраиваются в ванильный код, но скажем
	вы сделали свой лук и хотите использовать события ArrowNock и ArrowLoose?
	Вам нужно вставить их и зарегистрировать в шине событий в коде предмета.
 */

/** Событие ArrowNockEvent помещено в метод 'onItemRightClick' */
@Override
public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
{
	// Создание события и отправка его
	ArrowNockEvent event = new ArrowNockEvent(player, itemstack);
	MinecraftForge.EVENT_BUS.post(event);

	if (event.isCanceled())
	{
		// вы можете сделать что-нибудь другое, более полезное
		return event.result;
	}

	player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));

	return itemstack;
}

/** Событие ArrowLooseEvent помещено в метод 'onPlayerStoppedUsing' */
@Override
public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int par4)
{
	// Количество тиков использования предмета = максимальное время использования - par4, что равно максимальному времени использования - 1 за каждый тик использования
	int ticksInUse = this.getMaxItemUseDuration(itemstack) - par4;

	ArrowLooseEvent event = new ArrowLooseEvent(player, itemstack, ticksInUse);
	MinecraftForge.EVENT_BUS.post(event);

	if (event.isCanceled()) { return; }

	// ticksInUse может быть изменено Событием в вашем EventHandler'е, поэтому назначим здесь:
	ticksInUse = event.charge;

	// Делайте что хотите с предметом, например, призывайте огонь со стрелой или колдуйте огненые шары
}

/**
 * Часть 5: Добавление событий в ваш EventHandler
 */
/*
	Выбирите нужное вам событие из MinecraftForge, и добавьте его в свой EventHandler (обработчик событий) путем создания нового метод и установки события (Event'а) в качестве параметра метода.
	1.6: Используйте аннотацию "@ForgeSubscribe". Она вызывает событие в нужный момент.
	1.7: Используйте аннотацию "@SubscribeEvent". Она вызывает событие в нужный момент.
	Никогда, я повторю, никогда НЕ изменяйте классы форджа. Также, вам не требуется создание класса, унаследованного от класса события.
	Показанный шаблон подходит для любого метода с событием, который вы захотите сделать. Название метода делайте любое, но не забывайте про правильное название события.
	Доступ к переменным событий можно получить по примеру 'event.variableName'.
 */

@ForgeSubscribe
public void methodName(EventType event)
{
	// делайте здесь что хотите
}

/*
 *  Продвинутая Информация: Установка приоритета
 */
 
/*
	Заметьте, что приоритеты работают одинаково как в 1.6.х, так и в 1.7.х, отличие лишь в том, что аннотация меняется с @ForgeSubscribe на @SubscribeEvent.
	Приритет дает возможность определять обработчикам понимать в каком порядке читать слушателей событий. Слушатель событий это метод 
	с аннотацией @ForgeSubscribe (или @SubscrubeEvent для 1.7+) и если данный метод был зарегестрирован в MinecraftForge EVENT_BUS. 
	Когда вызывается множество одинаковых событий, игра выбирает какой слушатель событий (метод с событием) будет вызван первым. Это определяется
	по функциональности слушателя или по выставленному приоритету. Это может быть приминено среди отменяющих событий.
	Одино и тоже событие может быть вызванно множество раз из разных слушателей, с разными иминами и в разных обработчиках:
*/
@ForgeSubscribe
public void onLivingHurt(LivingHurtEvent event) {}

@ForgeSubscribe
public void onPlayerHurt(LivingHurtEvent event) {}

/*
	Оба метода будут вызываться по порядку регистрации в MinecraftForge.EVENT_BUS. Порядок может
	контролироваться добавлением (priority=VALUE) к аннотации @ForgeSubscribe, где VALUE - это один из вариантов
	из класса перечисления EventPriority. Приоритет HIGHEST вызывается первым, тогда как LOWEST последним.
*/

// этот слушатель событий будет вызван только после 'onPlayerHurt()'
@ForgeSubscribe(priority=LOWEST)
public void onLivingHurt(LivingHurtEvent event) {}

@ForgeSubscribe(priority=HIGHEST)
public void onPlayerHurt(LivingHurtEvent event) {}

/*
	Если два слушателя имеют одинаковые приоритеты, то они так же вызываются по порядку регистрации в коде.
	Для решения этого, слушатели могут быть помещены в разные обработчики (EventHandler) зарегистрированные в нужном порядке:
*/

// В данном варианте слушатель PlayerHurtHandler будет вызван первым
MinecraftForge.EVENT_BUS.register(new PlayerHurtHandler());
MinecraftForge.EVENT_BUS.register(new LivingHurtHandler());
// Для нескольких модов, вызывающих одно и тоже событие, порядок регистрации имеет такое же значение.

/*
 * Продвинутая Информация: Отменяемые События
 */
 
/*
	События с аннотацией @Cancelable имеют особый эффект при отмене. Если событие отмененно последующие слушатели с подобным
	событием не будут обрабатываться, пока не будет поставленна специальная аннотация:
*/

@ForgeSubscribe // стандартный приоритет (NORMAL)
public void onLivingHurt(LivingHurtEvent event) {
	// отменяем событие
	event.setCanceled(true);
}

@ForgeSubscribe(priority=LOWEST, receiveCanceled=true)
public void onPlayerHurt(LivingHurtEvent event) {
	// возобновляем событие
	event.setCanceled(false);
}

/*
	Благодаря контролированию порядка, в котором вызываются слушатели, можно избежать возобновления события, отмененного в другом слушателе.
	Хотя и бывают особые обстоятельства, в которых приходится приминять другие меры, дабы избежать логических ошибок.
*/

/*
	Теперь вы можете поиграться со слушателями событий и переменными событий, чтобы понять что и когда вызывается, но
	добавлю предупреждающее словечко: многие события могут быть вызваны только на ОДНОЙ из сторон (клиент / сервер), так что если что-то работает
	не так, как задуманно, проверьте на какой стороне может работать событие.
	Самый простой способ проверить вызывается ли метод, это поставить в начале каждого метода выводить сообщения в консоль об успешном выполнении:
*/

@ForgeSubscribe
public void someEventMethod(SomeEvent event) {
	System.out.println("Это событие вызванно; это клиентское событие? " + event.entity.worldObj.isRemote);
}

/*
	ВАЖНО: Последующие события из 1.6.х; многие из них не изменились в новых версиях, но есть и поменявшиеся. Всегда заглядывайте в пакет net.minecraftforge.event для просмотра доступных событий, доступных в версии, на который вы пишите модификацию.
	1. ArrowNockEvent
	Параметры: EntityPlayer player, ItemStack result
	Обычно вызывается из 'onItemRightClick'.
	Применение: Т.к. событие отменяемое, вы можете при несоблюдении условий (например, нет стрел в инвентаре) прекратить прицеливание из лука.
	2. ArrowLooseEvent
	Параметры: EntityPlayer player, ItemStack bow, int charge
	Обычно вызывается из 'onPlayerStoppedUsing'. Отменяем.
	Применение: Применяется в тандеме описанном свыше, для проверки отсутствия стрел. Если стрел нет - событие отменяется.
	3. EntityConstructing
	Параметры: Entity entity
	Вызывается с каждым объектом, если его конструктор вызван.
	Применение: Полезен, если вам нужно добавить расширенные свойства (ExtendedEntityProperties).
	4. EntityJoinWorldEvent
	Параметры: Entity entity, World world
	Вызывается когда любое существо попадает в мир первый раз.
	Применение: Полезен для синхронизации расширенных свойств (ExtendedEntityProperties), также для выдачи предметов при первом входе игрока и т.п.
	5. LivingUpdateEvent
	Параметры: EntityLivingBase entity
	Вызывается каждый тик в начале метода onUpdate() сущности.
	Применение: Скорее всего, это самое полезное событие. Вы можете позволить игроку летать, если он держит определенный предмет
	или одет в броню, вы можете изменить скорость падения игрока, вы можете добавить эффект зелья и многое другое, что сможете представить.
	Это и вправду полезное событие.
	6. LivingDropsEvent
	Параметры: EntityLivingBase entity, DamageSource source, ArrayList<EntityItem> drops, int lootingLevel, boolean
	recentlyHit, int specialDropValue
	Вызывается когда сущность умирает и выбрасывает предметы.
	Применение: Удобен если вы хотите изменить дроп с ванильных мобов или добавить особый дроп, если вы убиваете предметом
	из своего мода. Вы конечно можете удалить выпадающие предметы вообще. Полезно.
	7. LivingFallEvent
	Параметры: EntityLivingBase entity, float distance
	Вызывается, когда сущность касается земли после падения.
	ОБРАТИТЕ ВНИМАНИЕ: Это событие не вызывается в творческом режиме; PlayerFlyableFallEvent вместо него.
	Применение: Отменяем, так что 'event.setCanceled(true)' отключается обработку падения.
	Вы можете изменить высоту падения, но запомните, что событие срабатывает ТОЛЬКО при касании. Если хотите
	изменить высоту падения в определенных условия, лучше применить LivingUpdateEvent.
	Также, убедитесь что изменяете 'event.distance', а не 'entity.fallDistance', иначе вы не измените исход падения.
	8. LivingJumpEvent
	Параметры: EntityLivingBase entity
	Вызывается всегда, когда сущность прыгает.
	Применение: Полезно для 'entity.motionY += 10.0D'. Просто попробуйте.
	9. LivingAttackEvent
	Параметры: EntityLivingBase entity, DamageSource source, float ammount
	Вызывается, когда сущность атакует.
	Применение: Отменяемо. Здесь вы можете сделать предварительную обработку атаки перед событием LivingHurtEvent. Информация о
	источнике атаки находится в DamageSource, так что вы можете изменить нужные вам параметры урона. Изменения 
	равнозначны таким же в событии LivingHurtEvent, но здесь сделаны они будут раньше.
	10. LivingHurtEvent
	Параметры: EntityLivingBase entity, DamageSource source, float ammount
	Вызывается, когда сущность ранят.
	Применение: Супер полезное событие, если вам нужно сделать броню которая будет оборонять от огненного урона, увеличивать 
	урон от магии и т.д.
	11. LivingDeathEvent
	Параметры: EntityLivingBase entity, DamageSource source
	Вызывается, когда сущность умирает; отменяемо!
	Применение: Напомню, что DamageSource имеет множество переменных, таких как getEntity(), которая возвращает существо
	которое нанесло урон и исходя из этого - кто убийца. Можно отменить смерть и воскресить себя,
	или установить таймер для воскрешения. Если у вас есть свойства которые должны добавляться при смерти игрока,
	такие как IExtendedEntityProperties, вы можете добавить их здесь.
	12. EntityInteractEvent
	Параметры: EntityPlayer player, Entity target
	Вызывается, когда игрок жмет ПКМ по существу.
	Применение: Вы можете сделать много интересного с применением этого события. Одним из применений может оказаться сбор молока в ваше ведро...
	13. EntityItemPickupEvent
	Параметры: EntityPlayer player, EntityItem item
	Вызывается, когда игрок поднимает предмет
	Применение: Это полезное событие для тех предметов, которые должны как-то обрабатываться при подборе; 
	например, вы можете сделать что-то на подобие сфер опыта, сфер маны, которые восстанавливают ману, при подборе их с земли.
	14. HarvestCheck
	Параметры: EntityPlayer player, Block block, boolean success
	Вызывается в момент, когда игрок сломал блок, но дроп еще не выпал
	Применение: Может компоноваться с событием BreakSpeed, это, пожалуй, лучший способ изменить шахтерское ремесло.
*/
