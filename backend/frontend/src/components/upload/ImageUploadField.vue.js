import { ElMessage } from 'element-plus';
import { adminUploadApi } from '../../api/modules';
const props = defineProps();
const emit = defineEmits();
async function handleChange(uploadFile) {
    const raw = uploadFile.raw;
    if (!raw)
        return;
    const formData = new FormData();
    formData.append('file', raw);
    const response = await adminUploadApi.image(formData);
    emit('update:modelValue', response.data.url);
    ElMessage.success('上传成功');
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
const __VLS_0 = {}.ElUpload;
/** @type {[typeof __VLS_components.ElUpload, typeof __VLS_components.elUpload, typeof __VLS_components.ElUpload, typeof __VLS_components.elUpload, ]} */ ;
// @ts-ignore
const __VLS_1 = __VLS_asFunctionalComponent(__VLS_0, new __VLS_0({
    ...{ 'onChange': {} },
    showFileList: (false),
    autoUpload: (false),
    accept: "image/*",
}));
const __VLS_2 = __VLS_1({
    ...{ 'onChange': {} },
    showFileList: (false),
    autoUpload: (false),
    accept: "image/*",
}, ...__VLS_functionalComponentArgsRest(__VLS_1));
let __VLS_4;
let __VLS_5;
let __VLS_6;
const __VLS_7 = {
    onChange: (__VLS_ctx.handleChange)
};
var __VLS_8 = {};
__VLS_3.slots.default;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "cover-upload-card" },
});
if (__VLS_ctx.modelValue) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.img)({
        src: (__VLS_ctx.modelValue),
        alt: "cover",
        ...{ class: "cover-upload-image" },
    });
}
else {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "cover-upload-placeholder" },
    });
}
var __VLS_3;
/** @type {__VLS_StyleScopedClasses['cover-upload-card']} */ ;
/** @type {__VLS_StyleScopedClasses['cover-upload-image']} */ ;
/** @type {__VLS_StyleScopedClasses['cover-upload-placeholder']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            handleChange: handleChange,
        };
    },
    __typeEmits: {},
    __typeProps: {},
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
    __typeEmits: {},
    __typeProps: {},
});
; /* PartiallyEnd: #4569/main.vue */
