/* Форма заказа: добавление/удаление позиций, подстановка цены и пересчёт суммы */
(function () {
    'use strict';

    const body = document.getElementById('items-body');
    const template = document.getElementById('item-template');
    const totalEl = document.getElementById('order-total');
    if (!body || !template) return;

    function renumber() {
        body.querySelectorAll('tr').forEach((row, idx) => {
            row.querySelector('.row-num').textContent = idx + 1;
            row.querySelectorAll('[data-name]').forEach(el => {
                el.name = 'items[' + idx + '].' + el.dataset.name;
            });
        });
    }

    function recalc() {
        let total = 0;
        body.querySelectorAll('tr').forEach(row => {
            const qty = parseFloat(row.querySelector('[data-name="quantity"]').value) || 0;
            const price = parseFloat(row.querySelector('[data-name="unitPrice"]').value) || 0;
            const amount = qty * price;
            row.querySelector('.row-amount').textContent = window.formatMoney(amount);
            total += amount;
        });
        totalEl.textContent = window.formatMoney(total);
    }

    function bind(row) {
        const select = row.querySelector('[data-name="productId"]');
        const price = row.querySelector('[data-name="unitPrice"]');
        const unit = row.querySelector('.row-unit');
        const refreshUnit = () => {
            const opt = select.selectedOptions[0];
            unit.textContent = opt && opt.dataset.unit ? opt.dataset.unit : '';
        };
        select.addEventListener('change', () => {
            const opt = select.selectedOptions[0];
            if (opt && opt.dataset.price) price.value = opt.dataset.price;
            refreshUnit();
            recalc();
        });
        row.querySelectorAll('input').forEach(i => i.addEventListener('input', recalc));
        row.querySelector('.remove-row').addEventListener('click', () => {
            if (body.querySelectorAll('tr').length > 1) {
                row.remove();
            } else {
                row.querySelectorAll('input').forEach(i => i.value = '');
                select.value = '';
            }
            renumber();
            recalc();
        });
        refreshUnit();
    }

    document.getElementById('add-row').addEventListener('click', () => {
        const row = template.content.firstElementChild.cloneNode(true);
        body.appendChild(row);
        bind(row);
        renumber();
        row.querySelector('select').focus();
    });

    body.querySelectorAll('tr').forEach(bind);
    renumber();
    recalc();
})();
