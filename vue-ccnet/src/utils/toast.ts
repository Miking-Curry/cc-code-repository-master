// src/utils/toast.ts
import { createApp } from 'vue';
import ErrorToast from '@/components/toast/ErrorToast.vue';
import SuccessToast from '@/components/toast/SuccessToast.vue';

export function showError(message:string, duration = 3000) {
    const container = document.createElement('div');
    document.body.appendChild(container);

    const app = createApp(ErrorToast, { message, duration });
    const vm = app.mount(container);

    setTimeout(() => {
        app.unmount();
        container.remove();
    }, duration + 300); // 加上延迟的销毁时间
}

export function showSuccess(message: string, duration = 3000) {
    const container = document.createElement('div');
    document.body.appendChild(container);

    const app = createApp(SuccessToast, { message, duration });
    const vm = app.mount(container);

    setTimeout(() => {
        app.unmount();
        container.remove();
    }, duration + 300); // 加上延迟的销毁时间
}
