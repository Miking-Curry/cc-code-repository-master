import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';

export function useGoBack() {
    const router = useRouter();
    const userStore = useUserStore()
    const token = userStore.token

    const homePage = 'http://caicainet.com/#/jobs'
    const userPage = 'http://caicainet.com/#/user'
    const loginPage = 'http://caicainet.com/#/auth/login'

    const goBack = () => {
        const referrer = document.referrer;

        if (referrer && referrer.includes(location.hostname)) {
            // referrer 是本站页面
            const path = new URL(referrer).pathname;
            router.push(path); // 跳转回上一个路由页面
        } else if (referrer) {
            // 外部系统，直接跳转
            if (token) {
                const url = referrer
                window.location.href = url;
            } else {
                window.location.href = referrer;
            }

        } else {
            // 没有 referrer（用户直接输入页面、书签打开等），跳转首页或默认页 http://caicainet.com/#/jobs
            if (token) {
                const url = homePage
                window.location.href = url;
            } else {
                window.location.href = homePage;
            }
        }
    };

    const goBacktoUserPage = () => {
        window.location.href = userPage;
    };

    const goToLoginPage = () => {
        window.location.href = loginPage;
    }

    return {
        goBack,
        goBacktoUserPage,
        goToLoginPage
    };
}
