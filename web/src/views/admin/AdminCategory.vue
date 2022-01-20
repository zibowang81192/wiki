<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >

    <div class="admin-category">
      <h1>Manage Categories</h1>
    </div>
    <p>
      <a-form
          layout="inline"
          :model="param"
      >
        <a-form-item>
          <a-input v-model:value="param.name" placeholder="keyword">
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button
              type="primary"
              @click="handleQuery({page: 1,size: pagination.pageSize})"
          >
            查询
          </a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">
            新增
          </a-button>
        </a-form-item>
      </a-form>


    </p>
    <a-table :columns="columns"
             :row-key="record=> record.id"
             :data-source="categorys"
             :pagination="pagination"
             :loading="loading"
             @change="handleTableChange"
    >
      <template #cover="{text: cover}">
        <img v-if="cover" :src="cover" alt="avatar">
      </template>
      <template v-slot:action="{text, record}">
        <a-space size="small">
          <a-button type="primary" @click="edit(record)">
            编辑
          </a-button>
          <a-popconfirm
              title="Are you sure delete this task?"
              ok-text="Yes"
              cancel-text="No"
              @confirm="handleDelete(record.id)"
              @cancel="cancel"
          >
            <a-button type="danger">
              删除
            </a-button>
          </a-popconfirm>

        </a-space>
      </template>
    </a-table>

    <a-modal
        title="分类表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="name">
          <a-input v-model:value="category.name" />
        </a-form-item>
        <a-form-item label="parent">
          <a-input v-model:value="category.parent" />
        </a-form-item>
        <a-form-item label="sort">
          <a-input v-model:value="category.sort" />
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-content>

</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

const listData: Record<string, string>[] = [];

for (let i = 0; i < 23; i++) {
  listData.push({
    href: 'https://www.antdv.com/',
    title: `ant design vue part ${i}`,
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    description:
        'Ant Design, a design language for background applications, is refined by Ant UED Team.',
    content:
        'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
  });
}

export default defineComponent({
  name: 'AdminCategorys',

  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },

  setup() {
    console.log("setup");

    const param = ref();
    param.value={};

    const categorys = ref();
    // const pagination = {
    //   onChange: (page: number) => {
    //     console.log(page);
    //   },
    //   pageSize: 2,
    //   total: 0
    // };

    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading =ref(false);

    const columns = [
      {
        title: 'Name',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: 'Parent',
        dataIndex: 'parent',
        slots: {customRender: 'name'}
      },
      {
        title: 'Sort',
        dataIndex: 'sort',
        slots: {customRender: 'name'}
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    const handleQuery=(params:any)=>{
      loading.value = true;
      axios.get("/Category/list",
          {params: {
              page: params.page,
              size: params.size,
              name: param.value.name
            }}).then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          categorys.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
          //pagination.value.current = params.page;
        }
        else {
          message.error(data.message);
        }

      });
    }

    /**
     *  表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // const actions: Record<string, string>[] = [
    //   { type: 'StarOutlined', text: '156' },
    //   { type: 'LikeOutlined', text: '156' },
    //   { type: 'MessageOutlined', text: '7' },
    // ];

    /**
     *  编辑
     */
    const category = ref({});

    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/Category/save",
          category.value).then((response)=> {
        const data = response.data;
        if(data.success){  /** data的类型是CommonResp **/
          modalVisible.value = false;


          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
            name: param.value.name,
          });
        }
        else{
          message.error(data.message);
        }
        modalLoading.value = false;
      });
    };

    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    }

    const add = () => {
      modalVisible.value = true;
      category.value = {};
    }

    const handleDelete = (id: number) =>{
      let temp = 2
      axios.delete("/Category/delete/" + id).then((response) => {
        const data = response.data;
        if(data.success){
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          })
        }
      });
    };

    onMounted(()=>{
      console.log("onMounted");
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    })

    return {
      param,
      category,
      categorys,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,

    };
  },
});

</script>
<style>
.box {
  display: flex;
}
</style>
