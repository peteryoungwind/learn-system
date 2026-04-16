import { onMounted, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { userLearningApi } from '../../api/modules';
const route = useRoute();
const router = useRouter();
const categories = ref([]);
const albums = ref([]);
const materials = ref([]);
const loading = ref(false);
const deniedAdmin = ref(route.query.denied === 'admin');
const filters = reactive({
    categoryId: route.query.categoryId ? Number(route.query.categoryId) : undefined,
    albumId: route.query.albumId ? Number(route.query.albumId) : undefined,
    keyword: typeof route.query.keyword === 'string' ? route.query.keyword : '',
    fileType: typeof route.query.fileType === 'string' ? route.query.fileType : undefined,
});
function syncFromRoute() {
    filters.categoryId = route.query.categoryId ? Number(route.query.categoryId) : undefined;
    filters.albumId = route.query.albumId ? Number(route.query.albumId) : undefined;
    filters.keyword = typeof route.query.keyword === 'string' ? route.query.keyword : '';
    filters.fileType = typeof route.query.fileType === 'string' ? route.query.fileType : undefined;
    deniedAdmin.value = route.query.denied === 'admin';
}
async function loadBase() {
    const categoryResp = await userLearningApi.categories();
    categories.value = categoryResp.data;
}
async function loadAlbums() {
    const albumResp = await userLearningApi.albums(filters.categoryId);
    albums.value = albumResp.data;
}
async function loadMaterials() {
    loading.value = true;
    try {
        const materialResp = await userLearningApi.materials({
            categoryId: filters.categoryId,
            albumId: filters.albumId,
            keyword: filters.keyword,
        });
        const data = materialResp.data;
        materials.value = filters.fileType ? data.filter((item) => item.fileType === filters.fileType) : data;
    }
    finally {
        loading.value = false;
    }
}
function applyFilters() {
    router.push({
        path: '/search',
        query: {
            ...(filters.keyword ? { keyword: filters.keyword } : {}),
            ...(filters.categoryId ? { categoryId: String(filters.categoryId) } : {}),
            ...(filters.albumId ? { albumId: String(filters.albumId) } : {}),
            ...(filters.fileType ? { fileType: filters.fileType } : {}),
        },
    });
}
function resetFilters() {
    router.push({ path: '/search' });
}
function openDetail(row) {
    router.push(`/materials/${row.id}`);
}
watch(() => filters.categoryId, async (value, oldValue) => {
    if (value !== oldValue) {
        filters.albumId = undefined;
    }
    await loadAlbums();
});
watch(() => route.fullPath, async () => {
    syncFromRoute();
    await loadAlbums();
    await loadMaterials();
});
onMounted(async () => {
    await loadBase();
    await loadAlbums();
    await loadMaterials();
});
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "page-stack" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "section-head" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "eyebrow" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h1, __VLS_intrinsicElements.h1)({
    ...{ class: "page-title" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "page-subtitle" },
});
if (__VLS_ctx.deniedAdmin) {
    const __VLS_0 = {}.ElAlert;
    /** @type {[typeof __VLS_components.ElAlert, typeof __VLS_components.elAlert, ]} */ ;
    // @ts-ignore
    const __VLS_1 = __VLS_asFunctionalComponent(__VLS_0, new __VLS_0({
        title: "你没有管理员权限，已切换到用户端。",
        type: "warning",
        closable: (false),
        ...{ style: {} },
    }));
    const __VLS_2 = __VLS_1({
        title: "你没有管理员权限，已切换到用户端。",
        type: "warning",
        closable: (false),
        ...{ style: {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_1));
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "layout-grid" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.aside, __VLS_intrinsicElements.aside)({
    ...{ class: "filter-card sidebar-stack" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "filter-head" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "eyebrow" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h2, __VLS_intrinsicElements.h2)({
    ...{ class: "section-title" },
});
const __VLS_4 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({
    ...{ 'onKeyup': {} },
    modelValue: (__VLS_ctx.filters.keyword),
    placeholder: "关键词",
}));
const __VLS_6 = __VLS_5({
    ...{ 'onKeyup': {} },
    modelValue: (__VLS_ctx.filters.keyword),
    placeholder: "关键词",
}, ...__VLS_functionalComponentArgsRest(__VLS_5));
let __VLS_8;
let __VLS_9;
let __VLS_10;
const __VLS_11 = {
    onKeyup: (__VLS_ctx.applyFilters)
};
var __VLS_7;
const __VLS_12 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
    modelValue: (__VLS_ctx.filters.categoryId),
    placeholder: "分类",
    clearable: true,
}));
const __VLS_14 = __VLS_13({
    modelValue: (__VLS_ctx.filters.categoryId),
    placeholder: "分类",
    clearable: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_13));
__VLS_15.slots.default;
for (const [item] of __VLS_getVForSourceType((__VLS_ctx.categories))) {
    const __VLS_16 = {}.ElOption;
    /** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
    // @ts-ignore
    const __VLS_17 = __VLS_asFunctionalComponent(__VLS_16, new __VLS_16({
        key: (item.id),
        label: (item.name),
        value: (item.id),
    }));
    const __VLS_18 = __VLS_17({
        key: (item.id),
        label: (item.name),
        value: (item.id),
    }, ...__VLS_functionalComponentArgsRest(__VLS_17));
}
var __VLS_15;
const __VLS_20 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_21 = __VLS_asFunctionalComponent(__VLS_20, new __VLS_20({
    modelValue: (__VLS_ctx.filters.albumId),
    placeholder: "专辑",
    clearable: true,
}));
const __VLS_22 = __VLS_21({
    modelValue: (__VLS_ctx.filters.albumId),
    placeholder: "专辑",
    clearable: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_21));
__VLS_23.slots.default;
for (const [item] of __VLS_getVForSourceType((__VLS_ctx.albums))) {
    const __VLS_24 = {}.ElOption;
    /** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
    // @ts-ignore
    const __VLS_25 = __VLS_asFunctionalComponent(__VLS_24, new __VLS_24({
        key: (item.id),
        label: (item.name),
        value: (item.id),
    }));
    const __VLS_26 = __VLS_25({
        key: (item.id),
        label: (item.name),
        value: (item.id),
    }, ...__VLS_functionalComponentArgsRest(__VLS_25));
}
var __VLS_23;
const __VLS_28 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_29 = __VLS_asFunctionalComponent(__VLS_28, new __VLS_28({
    modelValue: (__VLS_ctx.filters.fileType),
    placeholder: "文件类型",
    clearable: true,
}));
const __VLS_30 = __VLS_29({
    modelValue: (__VLS_ctx.filters.fileType),
    placeholder: "文件类型",
    clearable: true,
}, ...__VLS_functionalComponentArgsRest(__VLS_29));
__VLS_31.slots.default;
const __VLS_32 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_33 = __VLS_asFunctionalComponent(__VLS_32, new __VLS_32({
    label: "PDF",
    value: "PDF",
}));
const __VLS_34 = __VLS_33({
    label: "PDF",
    value: "PDF",
}, ...__VLS_functionalComponentArgsRest(__VLS_33));
const __VLS_36 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_37 = __VLS_asFunctionalComponent(__VLS_36, new __VLS_36({
    label: "AUDIO",
    value: "AUDIO",
}));
const __VLS_38 = __VLS_37({
    label: "AUDIO",
    value: "AUDIO",
}, ...__VLS_functionalComponentArgsRest(__VLS_37));
const __VLS_40 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_41 = __VLS_asFunctionalComponent(__VLS_40, new __VLS_40({
    label: "VIDEO",
    value: "VIDEO",
}));
const __VLS_42 = __VLS_41({
    label: "VIDEO",
    value: "VIDEO",
}, ...__VLS_functionalComponentArgsRest(__VLS_41));
const __VLS_44 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_45 = __VLS_asFunctionalComponent(__VLS_44, new __VLS_44({
    label: "HTML",
    value: "HTML",
}));
const __VLS_46 = __VLS_45({
    label: "HTML",
    value: "HTML",
}, ...__VLS_functionalComponentArgsRest(__VLS_45));
var __VLS_31;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "action-row" },
});
const __VLS_48 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_49 = __VLS_asFunctionalComponent(__VLS_48, new __VLS_48({
    ...{ 'onClick': {} },
    type: "primary",
}));
const __VLS_50 = __VLS_49({
    ...{ 'onClick': {} },
    type: "primary",
}, ...__VLS_functionalComponentArgsRest(__VLS_49));
let __VLS_52;
let __VLS_53;
let __VLS_54;
const __VLS_55 = {
    onClick: (__VLS_ctx.applyFilters)
};
__VLS_51.slots.default;
var __VLS_51;
const __VLS_56 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_57 = __VLS_asFunctionalComponent(__VLS_56, new __VLS_56({
    ...{ 'onClick': {} },
}));
const __VLS_58 = __VLS_57({
    ...{ 'onClick': {} },
}, ...__VLS_functionalComponentArgsRest(__VLS_57));
let __VLS_60;
let __VLS_61;
let __VLS_62;
const __VLS_63 = {
    onClick: (__VLS_ctx.resetFilters)
};
__VLS_59.slots.default;
var __VLS_59;
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "panel-stack" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "ui-panel" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "summary-row" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
    ...{ class: "badge-soft" },
});
(__VLS_ctx.materials.length);
if (__VLS_ctx.filters.keyword) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "badge-neutral" },
    });
    (__VLS_ctx.filters.keyword);
}
if (__VLS_ctx.filters.fileType) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
        ...{ class: "badge-neutral" },
    });
    (__VLS_ctx.filters.fileType);
}
if (!__VLS_ctx.loading && !__VLS_ctx.materials.length) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "ui-panel empty-shell" },
    });
    const __VLS_64 = {}.ElEmpty;
    /** @type {[typeof __VLS_components.ElEmpty, typeof __VLS_components.elEmpty, ]} */ ;
    // @ts-ignore
    const __VLS_65 = __VLS_asFunctionalComponent(__VLS_64, new __VLS_64({
        description: "暂无匹配资料，请调整搜索条件",
    }));
    const __VLS_66 = __VLS_65({
        description: "暂无匹配资料，请调整搜索条件",
    }, ...__VLS_functionalComponentArgsRest(__VLS_65));
}
else {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "result-grid" },
    });
    __VLS_asFunctionalDirective(__VLS_directives.vLoading)(null, { ...__VLS_directiveBindingRestFields, value: (__VLS_ctx.loading) }, null, null);
    for (const [item] of __VLS_getVForSourceType((__VLS_ctx.materials))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
            ...{ onClick: (...[$event]) => {
                    if (!!(!__VLS_ctx.loading && !__VLS_ctx.materials.length))
                        return;
                    __VLS_ctx.openDetail(item);
                } },
            key: (item.id),
            ...{ class: "result-item" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({
            ...{ class: "result-title" },
        });
        (item.title);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
            ...{ class: "card-copy" },
        });
        (item.summary || '暂无摘要');
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "result-meta" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
        (item.author || '未知作者');
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
        (item.fileType);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
        (item.publishStatus);
    }
}
/** @type {__VLS_StyleScopedClasses['page-stack']} */ ;
/** @type {__VLS_StyleScopedClasses['section-head']} */ ;
/** @type {__VLS_StyleScopedClasses['eyebrow']} */ ;
/** @type {__VLS_StyleScopedClasses['page-title']} */ ;
/** @type {__VLS_StyleScopedClasses['page-subtitle']} */ ;
/** @type {__VLS_StyleScopedClasses['layout-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['filter-card']} */ ;
/** @type {__VLS_StyleScopedClasses['sidebar-stack']} */ ;
/** @type {__VLS_StyleScopedClasses['filter-head']} */ ;
/** @type {__VLS_StyleScopedClasses['eyebrow']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['action-row']} */ ;
/** @type {__VLS_StyleScopedClasses['panel-stack']} */ ;
/** @type {__VLS_StyleScopedClasses['ui-panel']} */ ;
/** @type {__VLS_StyleScopedClasses['summary-row']} */ ;
/** @type {__VLS_StyleScopedClasses['badge-soft']} */ ;
/** @type {__VLS_StyleScopedClasses['badge-neutral']} */ ;
/** @type {__VLS_StyleScopedClasses['badge-neutral']} */ ;
/** @type {__VLS_StyleScopedClasses['ui-panel']} */ ;
/** @type {__VLS_StyleScopedClasses['empty-shell']} */ ;
/** @type {__VLS_StyleScopedClasses['result-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['result-item']} */ ;
/** @type {__VLS_StyleScopedClasses['result-title']} */ ;
/** @type {__VLS_StyleScopedClasses['card-copy']} */ ;
/** @type {__VLS_StyleScopedClasses['result-meta']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            categories: categories,
            albums: albums,
            materials: materials,
            loading: loading,
            deniedAdmin: deniedAdmin,
            filters: filters,
            applyFilters: applyFilters,
            resetFilters: resetFilters,
            openDetail: openDetail,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
