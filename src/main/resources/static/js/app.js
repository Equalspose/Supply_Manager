/* Общие сценарии интерфейса */
(function () {
    'use strict';

    // Мобильное меню
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    document.querySelectorAll('[data-toggle-sidebar]').forEach(btn =>
        btn.addEventListener('click', () => {
            sidebar.classList.toggle('open');
            overlay.classList.toggle('show');
        }));
    if (overlay) overlay.addEventListener('click', () => {
        sidebar.classList.remove('open');
        overlay.classList.remove('show');
    });

    // Закрытие уведомлений (успешные скрываются сами через 5 секунд)
    document.querySelectorAll('.alert').forEach(alert => {
        const close = alert.querySelector('.close');
        const hide = () => {
            alert.style.transition = 'opacity .3s, transform .3s';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-6px)';
            setTimeout(() => alert.remove(), 300);
        };
        if (close) close.addEventListener('click', hide);
        if (alert.classList.contains('alert-success')) setTimeout(hide, 5000);
    });

    // Подтверждение опасных действий
    document.querySelectorAll('form[data-confirm]').forEach(form =>
        form.addEventListener('submit', e => {
            if (!confirm(form.dataset.confirm)) e.preventDefault();
        }));

    // Кликабельные строки таблиц
    document.querySelectorAll('tr[data-href]').forEach(row =>
        row.addEventListener('click', e => {
            if (e.target.closest('a, button, form, input, select')) return;
            window.location = row.dataset.href;
        }));

    // Автоотправка фильтров при выборе значения
    document.querySelectorAll('form[data-autosubmit] select').forEach(sel =>
        sel.addEventListener('change', () => sel.form.submit()));
})();

/* Форматирование денежных сумм */
window.formatMoney = function (value) {
    return new Intl.NumberFormat('ru-RU', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value || 0) + ' ₽';
};
