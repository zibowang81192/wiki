<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >

    <div class="admin-ebook">
      <h1>Manage Ebooks</h1>
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
             :data-source="ebooks"
             :pagination="pagination"
             :loading="loading"
             @change="handleTableChange"
    >
      <template #cover="{text: cover}">
        <img v-if="cover" :src="cover" alt="avatar">
      </template>
      <template v-slot:category="{ text, record}">
        <span>
          {{getCategoryName(record.category1Id)}} / {{getCategoryName(record.category2Id)}}
        </span>

      </template>
      <template v-slot:action="{text, record}">
        <a-space size="small">
          <router-link :to="'/admin/doc?ebookId=' + record.id">
            <a-button type="primary">
              文档管理
            </a-button>
          </router-link>

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
        title="电子书表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="cover">
          <a-input v-model:value="ebook.cover" />
        </a-form-item>
        <a-form-item label="name">
          <a-input v-model:value="ebook.name" />
        </a-form-item>
        <a-form-item label="category">
          <a-cascader
              v-model:value="categoryIds"
              :field-names="{label: 'name', value: 'id', children: 'children'}"
              :options="level1"
              style="width: 100%">
          </a-cascader>
        </a-form-item>
<!--        <a-form-item label="category 1">-->
<!--          <a-input v-model:value="ebook.category1Id" />-->
<!--        </a-form-item>-->
<!--        <a-form-item label="category 2">-->
<!--          <a-input v-model:value="ebook.category2Id" />-->
<!--        </a-form-item>-->
        <a-form-item label="description">
          <a-input v-model:value="ebook.description" type="textarea" />
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
        title: 'Category',
        slots: {customRender: 'category'}
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
        title: 'Description',
        dataIndex: 'description'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    //---------------- 表单 ---------------
    /**
     * 数组 [100, 101] 对应： 前端开发 / Vue
     */
    const categoryIds = ref();

    const handleQuery=(params:any)=>{
      loading.value = true;
      axios.get("/Ebook/list",
          {params: {
              page: params.page,
              size: params.size,
              name: param.value.name
            }}).then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          ebooks.value = data.content.list;
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
    const ebook = ref();

    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];

      axios.post("/Ebook/save",
          ebook.value).then((response)=> {
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
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    }

    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    }

    const handleDelete = (id: number) =>{
      let temp = 2
      axios.delete("/Ebook/delete/" + id).then((response) => {
        const data = response.data;
        if(data.success){
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          })
        }
      });
    };

    const level1 = ref();
    let categorys: any;

    const handleCategoryQuery=()=>{
      loading.value = true;
      axios.get("/Category/all").then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          categorys = data.content;
          console.log("原始数组: ", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        }
        else {
          message.error(data.message);
        }

      });
    }

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    onMounted(()=>{
      console.log("onMounted");
      handleCategoryQuery();
      // handleQuery({
      //   page: 1,
      //   size: pagination.value.pageSize
      // });
    })

    return {
      param,
      ebook,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,
      level1,
      categoryIds,

      edit,
      add,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,
      getCategoryName

    };
  },
});

</script>
<style>
.box {
  display: flex;
}
</style>
