import { computed, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { adminAlbumApi, adminCategoryApi, adminMaterialApi } from '../../api/modules';
import ImageUploadField from '../../components/upload/ImageUploadField.vue';
const router = useRouter();
const albums = ref([]);
const categories = ref([]);
const materials = ref([]);
const visible = ref(false);
const editingId = ref(null);
const form = reactive({
    categoryId: undefined,
    name: '',
    description: '',
    coverUrl: '',
    sortOrder: 0,
    status: 'ACTIVE',
});
const materialCountMap = computed(() => materials.value.reduce((acc, item) => {
    if (!item.albumId)
        return acc;
    acc[item.albumId] = (acc[item.albumId] || 0) + 1;
    return acc;
}, {}));
const categoryGroups = computed(() => categories.value
    .map((category) => ({
    category,
    albums: albums.value
        .filter((album) => album.categoryId === category.id)
        .sort((a, b) => a.sortOrder - b.sortOrder || a.id - b.id),
}))
    .filter((group) => group.albums.length > 0));
async function load() {
    const [albumResp, categoryResp, materialResp] = await Promise.all([
        adminAlbumApi.list(),
        adminCategoryApi.list(),
        adminMaterialApi.list(),
    ]);
    albums.value = albumResp.data;
    categories.value = categoryResp.data;
    materials.value = materialResp.data;
}
function resetForm() {
    Object.assign(form, {
        categoryId: categories.value[0]?.id,
        name: '',
        description: '',
        coverUrl: '',
        sortOrder: 0,
        status: 'ACTIVE',
    });
}
function openCreate() {
    editingId.value = null;
    resetForm();
    visible.value = true;
}
function openEdit(album) {
    editingId.value = album.id;
    Object.assign(form, {
        categoryId: album.categoryId,
        name: album.name,
        description: album.description || '',
        coverUrl: album.coverUrl || '',
        sortOrder: album.sortOrder ?? 0,
        status: album.status || 'ACTIVE',
    });
    visible.value = true;
}
async function submit() {
    const payload = {
        categoryId: form.categoryId,
        name: form.name,
        description: form.description,
        coverUrl: form.coverUrl || undefined,
        sortOrder: form.sortOrder,
        status: form.status,
    };
    if (editingId.value) {
        await adminAlbumApi.update(editingId.value, payload);
    }
    else {
        await adminAlbumApi.create(payload);
    }
    visible.value = false;
    ElMessage.success('保存成功');
    await load();
}
async function remove(id) {
    await ElMessageBox.confirm('删除后该专辑将不可恢复，是否继续？', '删除专辑', { type: 'warning' });
    await adminAlbumApi.remove(id);
    ElMessage.success('删除成功');
    await load();
}
function goToMaterials(id) {
    router.push(`/admin/albums/${id}/materials`);
}
function statusBadgeClass(status) {
    return status === 'ACTIVE' ? 'badge-success' : 'badge-warning';
}
onMounted(load);
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
__VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
    ...{ class: "ui-panel" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "toolbar toolbar-between" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "meta-row" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
    ...{ class: "badge-soft" },
});
(__VLS_ctx.albums.length);
__VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
    ...{ class: "badge-neutral" },
});
const __VLS_0 = {}.ElButton;
/** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
// @ts-ignore
const __VLS_1 = __VLS_asFunctionalComponent(__VLS_0, new __VLS_0({
    ...{ 'onClick': {} },
    type: "primary",
}));
const __VLS_2 = __VLS_1({
    ...{ 'onClick': {} },
    type: "primary",
}, ...__VLS_functionalComponentArgsRest(__VLS_1));
let __VLS_4;
let __VLS_5;
let __VLS_6;
const __VLS_7 = {
    onClick: (__VLS_ctx.openCreate)
};
__VLS_3.slots.default;
var __VLS_3;
if (!__VLS_ctx.categoryGroups.length) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "empty-shell" },
    });
    const __VLS_8 = {}.ElEmpty;
    /** @type {[typeof __VLS_components.ElEmpty, typeof __VLS_components.elEmpty, ]} */ ;
    // @ts-ignore
    const __VLS_9 = __VLS_asFunctionalComponent(__VLS_8, new __VLS_8({
        description: "暂无专辑，先创建一个专辑",
    }));
    const __VLS_10 = __VLS_9({
        description: "暂无专辑，先创建一个专辑",
    }, ...__VLS_functionalComponentArgsRest(__VLS_9));
}
else {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "album-group-stack" },
    });
    for (const [group] of __VLS_getVForSourceType((__VLS_ctx.categoryGroups))) {
        __VLS_asFunctionalElement(__VLS_intrinsicElements.section, __VLS_intrinsicElements.section)({
            key: (group.category.id),
            ...{ class: "album-group-block" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "summary-row album-group-head" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "album-group-copy" },
        });
        __VLS_asFunctionalElement(__VLS_intrinsicElements.h2, __VLS_intrinsicElements.h2)({
            ...{ class: "section-title" },
        });
        (group.category.name);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
            ...{ class: "card-copy" },
        });
        (group.category.description || '该分类下的专辑内容集合。');
        __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
            ...{ class: "badge-soft" },
        });
        (group.albums.length);
        __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
            ...{ class: "album-card-grid" },
        });
        for (const [album] of __VLS_getVForSourceType((group.albums))) {
            __VLS_asFunctionalElement(__VLS_intrinsicElements.article, __VLS_intrinsicElements.article)({
                key: (album.id),
                ...{ class: "album-card" },
            });
            __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
                ...{ class: "album-cover-shell" },
            });
            if (album.coverUrl) {
                __VLS_asFunctionalElement(__VLS_intrinsicElements.img)({
                    src: (album.coverUrl),
                    alt: "cover",
                    ...{ class: "album-cover-image" },
                });
            }
            else {
                __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
                    ...{ class: "album-cover-placeholder" },
                });
                (album.name.slice(0, 1));
            }
            __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
                ...{ class: "album-card-body" },
            });
            __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
                ...{ class: "summary-row" },
            });
            __VLS_asFunctionalElement(__VLS_intrinsicElements.h3, __VLS_intrinsicElements.h3)({
                ...{ class: "card-title" },
            });
            (album.name);
            __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({
                ...{ class: (__VLS_ctx.statusBadgeClass(album.status)) },
            });
            (album.status);
            __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
                ...{ class: "card-copy line-clamp-2" },
            });
            (album.description || '暂无专辑简介');
            __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
                ...{ class: "result-meta" },
            });
            __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
            (__VLS_ctx.materialCountMap[album.id] || 0);
            __VLS_asFunctionalElement(__VLS_intrinsicElements.span, __VLS_intrinsicElements.span)({});
            (album.sortOrder);
            __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
                ...{ class: "action-row" },
            });
            const __VLS_12 = {}.ElButton;
            /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
            // @ts-ignore
            const __VLS_13 = __VLS_asFunctionalComponent(__VLS_12, new __VLS_12({
                ...{ 'onClick': {} },
                text: true,
            }));
            const __VLS_14 = __VLS_13({
                ...{ 'onClick': {} },
                text: true,
            }, ...__VLS_functionalComponentArgsRest(__VLS_13));
            let __VLS_16;
            let __VLS_17;
            let __VLS_18;
            const __VLS_19 = {
                onClick: (...[$event]) => {
                    if (!!(!__VLS_ctx.categoryGroups.length))
                        return;
                    __VLS_ctx.openEdit(album);
                }
            };
            __VLS_15.slots.default;
            var __VLS_15;
            const __VLS_20 = {}.ElButton;
            /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
            // @ts-ignore
            const __VLS_21 = __VLS_asFunctionalComponent(__VLS_20, new __VLS_20({
                ...{ 'onClick': {} },
                text: true,
            }));
            const __VLS_22 = __VLS_21({
                ...{ 'onClick': {} },
                text: true,
            }, ...__VLS_functionalComponentArgsRest(__VLS_21));
            let __VLS_24;
            let __VLS_25;
            let __VLS_26;
            const __VLS_27 = {
                onClick: (...[$event]) => {
                    if (!!(!__VLS_ctx.categoryGroups.length))
                        return;
                    __VLS_ctx.goToMaterials(album.id);
                }
            };
            __VLS_23.slots.default;
            var __VLS_23;
            const __VLS_28 = {}.ElButton;
            /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
            // @ts-ignore
            const __VLS_29 = __VLS_asFunctionalComponent(__VLS_28, new __VLS_28({
                ...{ 'onClick': {} },
                text: true,
                type: "danger",
            }));
            const __VLS_30 = __VLS_29({
                ...{ 'onClick': {} },
                text: true,
                type: "danger",
            }, ...__VLS_functionalComponentArgsRest(__VLS_29));
            let __VLS_32;
            let __VLS_33;
            let __VLS_34;
            const __VLS_35 = {
                onClick: (...[$event]) => {
                    if (!!(!__VLS_ctx.categoryGroups.length))
                        return;
                    __VLS_ctx.remove(album.id);
                }
            };
            __VLS_31.slots.default;
            var __VLS_31;
        }
    }
}
const __VLS_36 = {}.ElDialog;
/** @type {[typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, typeof __VLS_components.ElDialog, typeof __VLS_components.elDialog, ]} */ ;
// @ts-ignore
const __VLS_37 = __VLS_asFunctionalComponent(__VLS_36, new __VLS_36({
    modelValue: (__VLS_ctx.visible),
    title: (__VLS_ctx.editingId ? '编辑专辑' : '新建专辑'),
    width: "680px",
}));
const __VLS_38 = __VLS_37({
    modelValue: (__VLS_ctx.visible),
    title: (__VLS_ctx.editingId ? '编辑专辑' : '新建专辑'),
    width: "680px",
}, ...__VLS_functionalComponentArgsRest(__VLS_37));
__VLS_39.slots.default;
const __VLS_40 = {}.ElForm;
/** @type {[typeof __VLS_components.ElForm, typeof __VLS_components.elForm, typeof __VLS_components.ElForm, typeof __VLS_components.elForm, ]} */ ;
// @ts-ignore
const __VLS_41 = __VLS_asFunctionalComponent(__VLS_40, new __VLS_40({
    model: (__VLS_ctx.form),
    labelWidth: "90px",
    ...{ class: "form-stack" },
}));
const __VLS_42 = __VLS_41({
    model: (__VLS_ctx.form),
    labelWidth: "90px",
    ...{ class: "form-stack" },
}, ...__VLS_functionalComponentArgsRest(__VLS_41));
__VLS_43.slots.default;
const __VLS_44 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_45 = __VLS_asFunctionalComponent(__VLS_44, new __VLS_44({
    label: "所属分类",
}));
const __VLS_46 = __VLS_45({
    label: "所属分类",
}, ...__VLS_functionalComponentArgsRest(__VLS_45));
__VLS_47.slots.default;
const __VLS_48 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_49 = __VLS_asFunctionalComponent(__VLS_48, new __VLS_48({
    modelValue: (__VLS_ctx.form.categoryId),
    ...{ class: "full-width" },
    placeholder: "请选择分类",
}));
const __VLS_50 = __VLS_49({
    modelValue: (__VLS_ctx.form.categoryId),
    ...{ class: "full-width" },
    placeholder: "请选择分类",
}, ...__VLS_functionalComponentArgsRest(__VLS_49));
__VLS_51.slots.default;
for (const [item] of __VLS_getVForSourceType((__VLS_ctx.categories))) {
    const __VLS_52 = {}.ElOption;
    /** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
    // @ts-ignore
    const __VLS_53 = __VLS_asFunctionalComponent(__VLS_52, new __VLS_52({
        key: (item.id),
        label: (item.name),
        value: (item.id),
    }));
    const __VLS_54 = __VLS_53({
        key: (item.id),
        label: (item.name),
        value: (item.id),
    }, ...__VLS_functionalComponentArgsRest(__VLS_53));
}
var __VLS_51;
var __VLS_47;
const __VLS_56 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_57 = __VLS_asFunctionalComponent(__VLS_56, new __VLS_56({
    label: "封面图",
}));
const __VLS_58 = __VLS_57({
    label: "封面图",
}, ...__VLS_functionalComponentArgsRest(__VLS_57));
__VLS_59.slots.default;
/** @type {[typeof ImageUploadField, ]} */ ;
// @ts-ignore
const __VLS_60 = __VLS_asFunctionalComponent(ImageUploadField, new ImageUploadField({
    modelValue: (__VLS_ctx.form.coverUrl),
}));
const __VLS_61 = __VLS_60({
    modelValue: (__VLS_ctx.form.coverUrl),
}, ...__VLS_functionalComponentArgsRest(__VLS_60));
var __VLS_59;
const __VLS_63 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_64 = __VLS_asFunctionalComponent(__VLS_63, new __VLS_63({
    label: "专辑名称",
}));
const __VLS_65 = __VLS_64({
    label: "专辑名称",
}, ...__VLS_functionalComponentArgsRest(__VLS_64));
__VLS_66.slots.default;
const __VLS_67 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_68 = __VLS_asFunctionalComponent(__VLS_67, new __VLS_67({
    modelValue: (__VLS_ctx.form.name),
    placeholder: "例如：Java 基础课程",
}));
const __VLS_69 = __VLS_68({
    modelValue: (__VLS_ctx.form.name),
    placeholder: "例如：Java 基础课程",
}, ...__VLS_functionalComponentArgsRest(__VLS_68));
var __VLS_66;
const __VLS_71 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_72 = __VLS_asFunctionalComponent(__VLS_71, new __VLS_71({
    label: "专辑简介",
}));
const __VLS_73 = __VLS_72({
    label: "专辑简介",
}, ...__VLS_functionalComponentArgsRest(__VLS_72));
__VLS_74.slots.default;
const __VLS_75 = {}.ElInput;
/** @type {[typeof __VLS_components.ElInput, typeof __VLS_components.elInput, ]} */ ;
// @ts-ignore
const __VLS_76 = __VLS_asFunctionalComponent(__VLS_75, new __VLS_75({
    modelValue: (__VLS_ctx.form.description),
    type: "textarea",
    rows: (3),
    placeholder: "填写专辑简介",
}));
const __VLS_77 = __VLS_76({
    modelValue: (__VLS_ctx.form.description),
    type: "textarea",
    rows: (3),
    placeholder: "填写专辑简介",
}, ...__VLS_functionalComponentArgsRest(__VLS_76));
var __VLS_74;
const __VLS_79 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_80 = __VLS_asFunctionalComponent(__VLS_79, new __VLS_79({
    label: "排序",
}));
const __VLS_81 = __VLS_80({
    label: "排序",
}, ...__VLS_functionalComponentArgsRest(__VLS_80));
__VLS_82.slots.default;
const __VLS_83 = {}.ElInputNumber;
/** @type {[typeof __VLS_components.ElInputNumber, typeof __VLS_components.elInputNumber, ]} */ ;
// @ts-ignore
const __VLS_84 = __VLS_asFunctionalComponent(__VLS_83, new __VLS_83({
    modelValue: (__VLS_ctx.form.sortOrder),
    min: (0),
}));
const __VLS_85 = __VLS_84({
    modelValue: (__VLS_ctx.form.sortOrder),
    min: (0),
}, ...__VLS_functionalComponentArgsRest(__VLS_84));
var __VLS_82;
const __VLS_87 = {}.ElFormItem;
/** @type {[typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, typeof __VLS_components.ElFormItem, typeof __VLS_components.elFormItem, ]} */ ;
// @ts-ignore
const __VLS_88 = __VLS_asFunctionalComponent(__VLS_87, new __VLS_87({
    label: "状态",
}));
const __VLS_89 = __VLS_88({
    label: "状态",
}, ...__VLS_functionalComponentArgsRest(__VLS_88));
__VLS_90.slots.default;
const __VLS_91 = {}.ElSelect;
/** @type {[typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, typeof __VLS_components.ElSelect, typeof __VLS_components.elSelect, ]} */ ;
// @ts-ignore
const __VLS_92 = __VLS_asFunctionalComponent(__VLS_91, new __VLS_91({
    modelValue: (__VLS_ctx.form.status),
    ...{ class: "full-width" },
}));
const __VLS_93 = __VLS_92({
    modelValue: (__VLS_ctx.form.status),
    ...{ class: "full-width" },
}, ...__VLS_functionalComponentArgsRest(__VLS_92));
__VLS_94.slots.default;
const __VLS_95 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_96 = __VLS_asFunctionalComponent(__VLS_95, new __VLS_95({
    label: "启用",
    value: "ACTIVE",
}));
const __VLS_97 = __VLS_96({
    label: "启用",
    value: "ACTIVE",
}, ...__VLS_functionalComponentArgsRest(__VLS_96));
const __VLS_99 = {}.ElOption;
/** @type {[typeof __VLS_components.ElOption, typeof __VLS_components.elOption, ]} */ ;
// @ts-ignore
const __VLS_100 = __VLS_asFunctionalComponent(__VLS_99, new __VLS_99({
    label: "停用",
    value: "DISABLED",
}));
const __VLS_101 = __VLS_100({
    label: "停用",
    value: "DISABLED",
}, ...__VLS_functionalComponentArgsRest(__VLS_100));
var __VLS_94;
var __VLS_90;
var __VLS_43;
{
    const { footer: __VLS_thisSlot } = __VLS_39.slots;
    const __VLS_103 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_104 = __VLS_asFunctionalComponent(__VLS_103, new __VLS_103({
        ...{ 'onClick': {} },
    }));
    const __VLS_105 = __VLS_104({
        ...{ 'onClick': {} },
    }, ...__VLS_functionalComponentArgsRest(__VLS_104));
    let __VLS_107;
    let __VLS_108;
    let __VLS_109;
    const __VLS_110 = {
        onClick: (...[$event]) => {
            __VLS_ctx.visible = false;
        }
    };
    __VLS_106.slots.default;
    var __VLS_106;
    const __VLS_111 = {}.ElButton;
    /** @type {[typeof __VLS_components.ElButton, typeof __VLS_components.elButton, typeof __VLS_components.ElButton, typeof __VLS_components.elButton, ]} */ ;
    // @ts-ignore
    const __VLS_112 = __VLS_asFunctionalComponent(__VLS_111, new __VLS_111({
        ...{ 'onClick': {} },
        type: "primary",
    }));
    const __VLS_113 = __VLS_112({
        ...{ 'onClick': {} },
        type: "primary",
    }, ...__VLS_functionalComponentArgsRest(__VLS_112));
    let __VLS_115;
    let __VLS_116;
    let __VLS_117;
    const __VLS_118 = {
        onClick: (__VLS_ctx.submit)
    };
    __VLS_114.slots.default;
    var __VLS_114;
}
var __VLS_39;
/** @type {__VLS_StyleScopedClasses['page-stack']} */ ;
/** @type {__VLS_StyleScopedClasses['section-head']} */ ;
/** @type {__VLS_StyleScopedClasses['eyebrow']} */ ;
/** @type {__VLS_StyleScopedClasses['page-title']} */ ;
/** @type {__VLS_StyleScopedClasses['page-subtitle']} */ ;
/** @type {__VLS_StyleScopedClasses['ui-panel']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar']} */ ;
/** @type {__VLS_StyleScopedClasses['toolbar-between']} */ ;
/** @type {__VLS_StyleScopedClasses['meta-row']} */ ;
/** @type {__VLS_StyleScopedClasses['badge-soft']} */ ;
/** @type {__VLS_StyleScopedClasses['badge-neutral']} */ ;
/** @type {__VLS_StyleScopedClasses['empty-shell']} */ ;
/** @type {__VLS_StyleScopedClasses['album-group-stack']} */ ;
/** @type {__VLS_StyleScopedClasses['album-group-block']} */ ;
/** @type {__VLS_StyleScopedClasses['summary-row']} */ ;
/** @type {__VLS_StyleScopedClasses['album-group-head']} */ ;
/** @type {__VLS_StyleScopedClasses['album-group-copy']} */ ;
/** @type {__VLS_StyleScopedClasses['section-title']} */ ;
/** @type {__VLS_StyleScopedClasses['card-copy']} */ ;
/** @type {__VLS_StyleScopedClasses['badge-soft']} */ ;
/** @type {__VLS_StyleScopedClasses['album-card-grid']} */ ;
/** @type {__VLS_StyleScopedClasses['album-card']} */ ;
/** @type {__VLS_StyleScopedClasses['album-cover-shell']} */ ;
/** @type {__VLS_StyleScopedClasses['album-cover-image']} */ ;
/** @type {__VLS_StyleScopedClasses['album-cover-placeholder']} */ ;
/** @type {__VLS_StyleScopedClasses['album-card-body']} */ ;
/** @type {__VLS_StyleScopedClasses['summary-row']} */ ;
/** @type {__VLS_StyleScopedClasses['card-title']} */ ;
/** @type {__VLS_StyleScopedClasses['card-copy']} */ ;
/** @type {__VLS_StyleScopedClasses['line-clamp-2']} */ ;
/** @type {__VLS_StyleScopedClasses['result-meta']} */ ;
/** @type {__VLS_StyleScopedClasses['action-row']} */ ;
/** @type {__VLS_StyleScopedClasses['form-stack']} */ ;
/** @type {__VLS_StyleScopedClasses['full-width']} */ ;
/** @type {__VLS_StyleScopedClasses['full-width']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            ImageUploadField: ImageUploadField,
            albums: albums,
            categories: categories,
            visible: visible,
            editingId: editingId,
            form: form,
            materialCountMap: materialCountMap,
            categoryGroups: categoryGroups,
            openCreate: openCreate,
            openEdit: openEdit,
            submit: submit,
            remove: remove,
            goToMaterials: goToMaterials,
            statusBadgeClass: statusBadgeClass,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
