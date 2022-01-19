<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
    <div class="admin-ebook">
      <h1>Manage Ebooks</h1>
    </div>
    <a-table :columns="columns"
             :row-key="record=> record.id"
             :data-source="ebooks"
             :pagination="pagination"
             :loading="loading"
             @change="handleTableChange"
    >
      <template #cover="{text: cover}">
        <img v-if="cover" :src="cover" alt="avatar">
      </template>
      <template v-slot:action="{text, record}">
        <a-space size="small">
          <a-button type="primary" @click="edit">
            编辑
          </a-button>
          <a-button type="danger">
            删除
          </a-button>
        </a-space>
      </template>
    </a-table>

    <a-modal
        title="电子书表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <p>Test</p>
    </a-modal>

  </a-layout-content>

</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import axios from 'axios';
import {message} from "ant-design-vue";

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
  name: 'AdminEbooks',

  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },

  setup() {
    console.log("setup");

    const param = ref();
    param.value={};

    const ebooks = ref();
    // const pagination = {
    //   onChange: (page: number) => {
    //     console.log(page);
    //   },
    //   pageSize: 2,
    //   total: 0
    // };

    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading =ref(false);

    const columns = [
      {
      title: 'Cover',
      dataIndex: 'cover',
      slots: {customRender: 'cover'}
      },
      {
        title: 'Name',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      {
        title: 'Category 1',
        dataIndex: 'category_id_1',
        slots: {customRender: 'name'}
      },
      {
        title: 'Category 2',
        dataIndex: 'category_id_2',
        slots: {customRender: 'name'}
      },
      {
        title: 'Star',
        dataIndex: 'star_count'
      },
      {
        title: 'View',
        dataIndex: 'view_count'
      },
      {
        title: 'Comment',
        dataIndex: 'comment_count'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    const handleQuery=(params:any)=>{
      loading.value = true;
      axios.get("/Ebook/list",
          {params: {
              page: params.page,
              size: params.size
            }}).then((response)=>{
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;
        // 重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
        //pagination.value.current = params.page;
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

    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };

    const edit = () => {
      modalVisible.value = true;
    }

    onMounted(()=>{
      console.log("onMounted");
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    })

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,
      modalVisible,
      modalLoading,
      handleModalOk

    };
  },
});

</script>
<style>
.box {
  display: flex;
}
</style>
