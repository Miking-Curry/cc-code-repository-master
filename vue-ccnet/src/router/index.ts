import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '../stores/user'
import { showNeedLoginDialog } from '@/utils/globalDialog'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/point',
        name: 'point',
        component: () => import('../views/point-views/Point.vue'),
        meta: { title: '积分中心' },
    },
    {
        path: '/point/point-rule',
        name: 'point-rule',
        component: () => import('@/views/point-views/PointRule.vue'),
        meta: { title: '积分规则' }
    },
    {
        path: '/ai',
        name: 'Ai',
        component: () => import('@/views/ai-views/Ai.vue'),
        meta: { title: 'AI帮你找' },
    },
    {
        path: '/credit/identity',
        name: 'identity',
        component: () => import('@/views/credit-views/identity.vue'),
        meta: { title: '身份证信息填写' },
    },
    {
        path: '/credit/graduation',
        name: 'graduation',
        component: () => import('@/views/credit-views/graduation.vue'),
        meta: { title: '毕业信息填写' },
    },
    {
        path: '/credit/work',
        name: 'work',
        component: () => import('@/views/credit-views/work.vue'),
        meta: { title: '工作经历信息填写' },
    },
    {
        path: '/credit/resign',
        name: 'resign',
        component: () => import('@/views/credit-views/resign.vue'),
        meta: { title: '离职信息填写' },
    },
    {
        path: '/credit/skill',
        name: 'skill',
        component: () => import('@/views/credit-views/skill.vue'),
        meta: { title: '证书信息填写' },
    },
    {
        path: '/credit',
        name: 'credit',
        component: () => import('@/views/credit-views/credit.vue'),
        meta: { title: '信用等级' },
    },
    {
        path: '/ai/improve',
        name: 'improve',
        component: () => import('@/views/ai-views/improve.vue'),
        meta: { title: 'AI帮你找' },
    },
    {
        path: '/better',
        name: 'Better',
        component: () => import('@/views/better-views/Better.vue'),
        meta: { title: '精选' },
    },
    {
        path: '/test',
        name: 'Test',
        component: () => import('@/views/test-views/Test.vue'),
        meta: { title: '测试' },
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    // 设置页面标题
    if (to.meta.title) {
        document.title = to.meta.title as string
    }

    const token = to.query.token
    if (token) {
        const userStore = useUserStore()
        userStore.setToken(token as string)
        // token: _ 的含义是：
        // 把 token 从对象中提取出来。
        // 给它起一个名字叫 _，然后你根本不使用它（表示丢弃）。
        // ...query 是剩余属性收集：把除了 token 之外的所有属性收集进 query 对象中。
        const { token: _, ...query } = to.query
        next({ path: to.path, query })
    } else {
        //在开发环境下，只要打开页面，就设置了超级token
        // 超级Token，开发环境使用，记得移除。TODO
        // const userStore = useUserStore()
        // userStore.setToken(`eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiNDg2MiIsImlhdCI6MTc1MjA0Mzk1OCwiZXhwIjoxNzUyMDQ3NTU4fQ.g3KtO0sYxoqYsqE5YiGLmPqHzjpl_Tsr23hoVbNlK7M`)
        // next()

        //生产环境下，请打开下面的代码
        //假如LocalStorage存有token
        const userStore = useUserStore()
        const hasToken = userStore.token

        if (!hasToken) {
            showNeedLoginDialog()
        } else {
            // 有 token，正常放行
            next()
        }
    }
})

export default router 