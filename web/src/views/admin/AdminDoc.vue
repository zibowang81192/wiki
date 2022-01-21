<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >

    <div class="admin-doc">
      <h1>Manage Documents</h1>
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
              @click="handleQuery()"
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
             :data-source="level1"
             :loading="loading"
             :pagination="false"
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
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="name">
          <a-input v-model:value="doc.name" />
        </a-form-item>
        <a-form-item label="parent">
          <a-input v-model:value="doc.parent" />
          <a-select
              ref="select"
              v-model:value="doc.parent"
              style="width: 120px"
              @focus="focus"
              @change="handleChange"
          >
            <a-select-option value="0">无</a-select-option>
            <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
              {{c.name}}
            </a-select-option>

          </a-select>

        </a-form-item>
        <a-form-item label="sort">
          <a-input v-model:value="doc.sort" />
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
  name: 'AdminDocs',

  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },

  setup() {
    console.log("setup");

    const level1 =  ref();

    const param = ref();
    param.value={};

    const docs = ref();

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

    const handleQuery=()=>{
      loading.value = true;
      axios.get("/Doc/all").then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          docs.value = data.content;
          console.log("原始数组: ", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构：", level1.value);
        }
        else {
          message.error(data.message);
        }

      });
    }

    /**
     *  编辑
     */
    const doc = ref({});

    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/Doc/save",
          doc.value).then((response)=> {
        const data = response.data;
        if(data.success){  /** data的类型是CommonResp **/
          modalVisible.value = false;


          //重新加载列表
          handleQuery();
        }
        else{
          message.error(data.message);
        }
        modalLoading.value = false;
      });
    };

    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);
    }

    const add = () => {
      modalVisible.value = true;
      doc.value = {};
    }

    const handleDelete = (id: number) =>{
      let temp = 2
      axios.delete("/Doc/delete/" + id).then((response) => {
        const data = response.data;
        if(data.success){
          handleQuery()
        }
      });
    };

    onMounted(()=>{
      console.log("onMounted");
      handleQuery();
    })

    return {
      param,
      doc,
      //docs,
      level1,
      columns,
      loading,
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
