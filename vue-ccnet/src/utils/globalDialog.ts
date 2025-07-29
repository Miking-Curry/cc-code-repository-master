// utils/globalDialog.ts
import { createApp, h } from 'vue'
import NeedLoginDialog from '@/components/toast/NeedLoginDialog.vue'
import UnderDevelopDialog from '@/components/toast/UnderDevelopDialog.vue'

let instance: any = null

export function showNeedLoginDialog() {
    if (instance) {
        instance.openDialog()
    } else {
        const container = document.createElement('div')
        document.body.appendChild(container)

        const app = createApp({
            render() {
                return h(NeedLoginDialog, { ref: 'dialogRef' })
            },
            mounted() {
                instance = this.$refs.dialogRef
                instance.openDialog()
            }
        })

        app.mount(container)
    }
}

export function showUnderDevelopDialog() {
    if (instance) {
        instance.openDialog()
    } else {
        const container = document.createElement('div')
        document.body.appendChild(container)

        const app = createApp({
            render() {
                return h(UnderDevelopDialog, { ref: 'dialogRef' })
            },
            mounted() {
                instance = this.$refs.dialogRef
                instance.openDialog()
            }
        })

        app.mount(container)
    }
}
