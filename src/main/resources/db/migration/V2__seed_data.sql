-- Демонстрационные данные (условные, для примера ПАО «Ростелеком»)

INSERT INTO category (name, description) VALUES
 ('Абонентское оборудование',      'Оборудование, устанавливаемое у абонентов: ONT, роутеры, ТВ-приставки'),
 ('Активное сетевое оборудование', 'Коммутаторы, маршрутизаторы, OLT, модули SFP'),
 ('Кабельная продукция',           'Оптические и медные кабели, патч-корды'),
 ('Пассивное оптическое оборудование', 'Сплиттеры, муфты, кроссы, оптические розетки'),
 ('Электропитание',                'ИБП, аккумуляторные батареи, блоки питания'),
 ('Монтажные материалы',           'Шкафы, крепёж, кабельные стяжки, инструмент');

INSERT INTO supplier (name, inn, kpp, contact_person, phone, email, address, rating, active) VALUES
 ('ООО «ТелекомСнаб»',            '7701234567', '770101001', 'Смирнов Алексей Петрович',   '+7 (495) 123-45-67', 'sales@telecomsnab.ru',  'г. Москва, ул. Профсоюзная, д. 65',            4.8, TRUE),
 ('АО «Связьоборудование»',       '7802345678', '780201001', 'Кузнецова Ирина Олеговна',   '+7 (812) 234-56-78', 'info@svyazob.ru',       'г. Санкт-Петербург, Лиговский пр., д. 50',      4.5, TRUE),
 ('ООО «ОптикЛайн»',              '5403456789', '540301001', 'Попов Дмитрий Сергеевич',    '+7 (383) 345-67-89', 'order@opticline.ru',    'г. Новосибирск, ул. Кирова, д. 113',           4.2, TRUE),
 ('ООО «СетьТехИмпорт»',          '6604567890', '660401001', 'Васильева Анна Игоревна',    '+7 (343) 456-78-90', 'zakaz@stimport.ru',     'г. Екатеринбург, ул. Малышева, д. 51',         3.9, TRUE),
 ('АО «ЭнергоПитание»',           '2305678901', '230501001', 'Морозов Игорь Викторович',   '+7 (861) 567-89-01', 'ups@energopit.ru',      'г. Краснодар, ул. Красная, д. 122',            4.6, TRUE),
 ('ООО «КабельПромРесурс»',       '5006789012', '500601001', 'Новикова Елена Андреевна',   '+7 (496) 678-90-12', 'cable@kpr.ru',          'Московская обл., г. Подольск, ул. Комсомольская, д. 1', 4.4, TRUE),
 ('ООО «МонтажКомплект»',         '1607890123', '160701001', 'Фёдоров Руслан Ильдарович',  '+7 (843) 789-01-23', 'mk@montazhkomplekt.ru', 'г. Казань, ул. Баумана, д. 19',               3.7, TRUE),
 ('ООО «ЦифраДистрибуция»',       '7708901234', '770801001', 'Лебедев Максим Юрьевич',     '+7 (495) 890-12-34', 'b2b@cifradistr.ru',     'г. Москва, Варшавское ш., д. 118',            3.2, FALSE);

INSERT INTO product (sku, name, category_id, unit, price, min_stock, description) VALUES
 ('ONT-GPON-01',  'Оптический терминал ONT GPON (1 порт GE)',       1, 'шт',   2450.00, 300, 'Абонентский терминал для подключения по технологии GPON'),
 ('ONT-GPON-WF6', 'Оптический терминал ONT GPON с Wi-Fi 6',         1, 'шт',   5900.00, 250, 'ONT со встроенным двухдиапазонным Wi-Fi 6 и 4 портами GE'),
 ('RTR-WF6-AX3',  'Wi-Fi роутер AX3000 двухдиапазонный',            1, 'шт',   4300.00, 200, 'Роутер для абонентов FTTB'),
 ('STB-IPTV-4K',  'ТВ-приставка IPTV 4K (Android TV)',              1, 'шт',   3800.00, 150, 'Приставка для интерактивного ТВ'),
 ('SW-ACC-24G',   'Коммутатор доступа 24×GE + 4×SFP+',             2, 'шт',  48500.00,  20, 'Управляемый коммутатор L2+ для узлов доступа'),
 ('SW-AGG-48X',   'Коммутатор агрегации 48×SFP+ + 6×QSFP28',       2, 'шт', 412000.00,   3, 'Коммутатор уровня агрегации L3'),
 ('OLT-16P',      'Станционный терминал OLT GPON 16 портов',        2, 'шт', 365000.00,   2, 'OLT для построения сетей PON'),
 ('SFP-10G-LR',   'Модуль SFP+ 10G LR 1310 нм 10 км',               2, 'шт',   2900.00, 100, 'Оптический трансивер одномодовый'),
 ('CBL-OKL-8',    'Кабель оптический ОКЛ-8 (8 волокон)',            3, 'м',      18.50, 20000, 'Кабель для прокладки в кабельной канализации'),
 ('CBL-OKSN-16',  'Кабель оптический самонесущий ОКСН-16',          3, 'м',      34.00, 15000, 'Подвесной кабель на опоры ЛЭП и связи'),
 ('CBL-UTP5E',    'Кабель UTP cat.5e 4×2×0,52',                     3, 'м',      14.20, 30000, 'Медная витая пара для FTTB'),
 ('PC-SCAPC-3',   'Патч-корд оптический SC/APC–SC/APC 3 м',         3, 'шт',    180.00, 1000, 'Одномодовый симплексный патч-корд'),
 ('SPL-PLC-1x8',  'Сплиттер PLC 1×8 SC/APC',                        4, 'шт',    950.00, 300, 'Планарный оптический разветвитель'),
 ('MFT-OPT-48',   'Муфта оптическая тупиковая на 48 волокон',       4, 'шт',   3200.00,  60, 'Муфта для соединения оптических кабелей'),
 ('ODF-24-SC',    'Кросс оптический стоечный 24 порта SC',          4, 'шт',   6700.00,  30, 'Оптический распределительный кросс 1U'),
 ('UPS-3KVA-RM',  'ИБП онлайн 3 кВА стоечный',                      5, 'шт',  87000.00,  10, 'Источник бесперебойного питания для узлов связи'),
 ('BAT-12V-100',  'Аккумуляторная батарея 12 В 100 А·ч',            5, 'шт',  16500.00,  40, 'Свинцово-кислотная AGM батарея'),
 ('CAB-19-42U',   'Шкаф телекоммуникационный 19" 42U',              6, 'шт',  52000.00,   8, 'Напольный шкаф 600×1000 мм'),
 ('CAB-19-9U',    'Шкаф настенный 19" 9U антивандальный',           6, 'шт',  14800.00,  25, 'Настенный шкаф для подъездов'),
 ('TIE-300-100',  'Стяжки кабельные 300 мм (упаковка 100 шт)',      6, 'уп',    210.00, 500, 'Нейлоновые стяжки, чёрные, УФ-стойкие');

INSERT INTO warehouse (code, name, region, address, manager, capacity) VALUES
 ('MSK-01', 'Центральный склад «Москва»',          'Центр',         'г. Москва, ул. Гончарова, д. 17',               'Орлов Сергей Николаевич',   120000),
 ('SPB-01', 'Региональный склад «Северо-Запад»',   'Северо-Запад',  'г. Санкт-Петербург, ул. Седова, д. 11',          'Белова Марина Викторовна',  60000),
 ('NSK-01', 'Региональный склад «Сибирь»',         'Сибирь',        'г. Новосибирск, ул. Станционная, д. 30',         'Ковалёв Артём Олегович',    45000),
 ('EKB-01', 'Региональный склад «Урал»',           'Урал',          'г. Екатеринбург, ул. Фронтовых Бригад, д. 18',   'Зайцева Ольга Петровна',    50000),
 ('KRD-01', 'Региональный склад «Юг»',             'Юг',            'г. Краснодар, ул. Уральская, д. 75',             'Гусейнов Тимур Рашидович',  40000);

-- Остатки на складах: центральный склад хранит ~40% запаса, региональные — по ~15%.
-- Несколько позиций намеренно ниже минимального остатка, чтобы показать раздел «Дефицит».
SELECT setseed(0.42);
INSERT INTO stock (product_id, warehouse_id, quantity)
SELECT p.id, w.id,
       GREATEST(0, round(p.min_stock
             * CASE WHEN p.sku IN ('ONT-GPON-WF6', 'SFP-10G-LR', 'CBL-OKSN-16', 'SPL-PLC-1x8', 'BAT-12V-100', 'OLT-16P')
                    THEN 0.35 + random() * 0.4
                    ELSE 1.2 + random() * 1.3 END
             * CASE WHEN w.code = 'MSK-01' THEN 0.4 ELSE 0.15 END
             * (0.8 + random() * 0.4))::int)
FROM product p CROSS JOIN warehouse w;

-- Генерация истории заказов за последний год
DO $$
DECLARE
    i            INT;
    j            INT;
    n_items      INT;
    v_order_id   BIGINT;
    v_supplier   BIGINT;
    v_warehouse  BIGINT;
    v_product    RECORD;
    v_date       DATE;
    v_status     VARCHAR(30);
    v_qty        INT;
    v_recv       INT;
    v_deliv_id   BIGINT;
    v_deliv_no   INT := 0;
    v_age        INT;
BEGIN
    FOR i IN 1..60 LOOP
        v_age := (i - 1) * 6 + floor(random() * 5)::int;
        v_date := CURRENT_DATE - v_age;
        SELECT id INTO v_supplier  FROM supplier  WHERE active ORDER BY random() LIMIT 1;
        SELECT id INTO v_warehouse FROM warehouse ORDER BY random() LIMIT 1;

        IF v_age < 6 THEN
            v_status := CASE WHEN random() < 0.5 THEN 'DRAFT' ELSE 'APPROVED' END;
        ELSIF v_age < 20 THEN
            v_status := CASE WHEN random() < 0.5 THEN 'SENT' ELSE 'PARTIALLY_DELIVERED' END;
        ELSE
            v_status := CASE WHEN random() < 0.1 THEN 'CANCELLED' ELSE 'DELIVERED' END;
        END IF;

        INSERT INTO purchase_order (number, supplier_id, warehouse_id, status, order_date, expected_date, comment, created_by, created_at)
        VALUES ('ЗП-' || to_char(v_date, 'YYYY') || '-' || lpad(i::text, 4, '0'),
                v_supplier, v_warehouse, v_status, v_date, v_date + 14,
                'Плановая закупка для развития сети', 'admin', v_date)
        RETURNING id INTO v_order_id;

        n_items := 2 + floor(random() * 3)::int;
        FOR v_product IN SELECT id, price, min_stock FROM product ORDER BY random() LIMIT n_items LOOP
            v_qty := GREATEST(1, (v_product.min_stock * (0.2 + random() * 0.6))::int);
            v_recv := CASE v_status
                        WHEN 'DELIVERED' THEN v_qty
                        WHEN 'PARTIALLY_DELIVERED' THEN v_qty / 2
                        ELSE 0 END;
            INSERT INTO order_item (order_id, product_id, quantity, unit_price, received_qty)
            VALUES (v_order_id, v_product.id, v_qty, v_product.price, v_recv);
        END LOOP;

        UPDATE purchase_order po
           SET total_amount = (SELECT COALESCE(SUM(quantity * unit_price), 0) FROM order_item WHERE order_id = po.id)
         WHERE po.id = v_order_id;

        IF v_status IN ('DELIVERED', 'PARTIALLY_DELIVERED') THEN
            v_deliv_no := v_deliv_no + 1;
            INSERT INTO delivery (number, order_id, delivery_date, waybill, carrier, status, comment, received_by, created_at)
            VALUES ('ПТ-' || to_char(v_date, 'YYYY') || '-' || lpad(v_deliv_no::text, 4, '0'),
                    v_order_id, LEAST(CURRENT_DATE, v_date + 5 + floor(random() * 10)::int),
                    'ТН-' || (100000 + floor(random() * 899999)::int),
                    (ARRAY['ПЭК', 'Деловые Линии', 'СДЭК', 'Собственный транспорт поставщика'])[1 + floor(random() * 4)::int],
                    CASE WHEN random() < 0.85 THEN 'ACCEPTED' ELSE 'ACCEPTED_WITH_ISSUES' END,
                    NULL, 'admin', v_date + 5)
            RETURNING id INTO v_deliv_id;

            INSERT INTO delivery_item (delivery_id, order_item_id, quantity)
            SELECT v_deliv_id, oi.id, oi.received_qty FROM order_item oi
             WHERE oi.order_id = v_order_id AND oi.received_qty > 0;
        END IF;
    END LOOP;
END $$;
