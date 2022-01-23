<template>
  <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
    <a-row>
      <a-col :span="6">
        <a-tree
          v-if="level1.length > 0"
          :tree-data="level1"
          @select="onSelect"
          :replaceFields="{title: 'name', key: 'id', value: 'id'}"
          :defaultExpandAll="true"
        >


        </a-tree>

      </a-col>
      <a-col :span="18">
        <div class="wangeditor" :innerHTML="html"></div>

      </a-col>
    </a-row>

  </a-layout-content>
</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import axios from 'axios';
import {message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from "@ant-design/icons";
import E from 'wangeditor';

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
  name: 'Docs',

  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    ExclamationCircleOutlined,
  },

  setup() {
    console.log("setup");

    const route = useRoute();

    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);

    const level1 =  ref();

    level1.value = [];

    const param = ref();
    param.value={};

    const docs = ref();

    const html = ref();

    const loading =ref(false);

    const columns = [
      {
        title: 'Name',
        dataIndex: 'name',
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
      level1.value = [];
      axios.get("/Doc/all/" + route.query.ebookId).then((response)=>{
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
     * 查找content
     */
    const handleContentQuery=()=>{
      axios.get("/Doc/find-content/" + doc.value.id).then((response)=>{
        const data = response.data;
        if(data.success){
          editor.txt.html(data.content);
        }
        else {
          message.error(data.message);
        }

      });
    }

    /**
     *  编辑
     */
    const doc = ref();
    doc.value = {};
    const treeSelectData = ref();
    treeSelectData.value = [];

    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const editor = new E('#content');
    editor.config.zIndex = 0;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/Doc/save",
          doc.value).then((response)=> {
        const data = response.data;
        if(data.success){  /** data的类型是CommonResp **/
        //modalVisible.value = false;
        message.success("保存成功");

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

      editor.txt.html("");

      modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleContentQuery()

      //不能选择自身及其子节点作为父节点
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个“无”
      treeSelectData.value.unshift({id: 0, name: '无'});

      // setTimeout(function (){
      //   editor.create();
      // },100);
      editor.create();
    }

    const add = () => {

      editor.txt.html("");

      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };
      //不能选择自身及其子节点作为父节点
      treeSelectData.value = Tool.copy(level1.value);
      // 为选择树添加一个“无”
      treeSelectData.value.unshift({id: 0, name: '无'});
      // setTimeout(function (){
      //   editor.create();
      // },100);
      editor.create();

    }

    const handleDelete = (id: number) =>{
      getDeleteIds(level1.value, id);
      let temp = 2;
      axios.delete("/Doc/delete/" + ids.join(",")).then((response) => {
        const data = response.data;
        if(data.success){
          handleQuery()
        }
      });
    };

    /**
     * 内容查询
     */
    const handleQueryContent = (id: number) => {
      axios.get("/Doc/find-content/" + id).then((response) =>{
        const data = response.data;
        if(data.success) {
          html.value = data.content;
        }
        else{
          message.error(data.message);
        }
      })
    }

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)) {
        handleQueryContent(selectedKeys[0]);
      }
    }

    /**
     * 二次确认删除
     */
    const names: Array<string> = [];

    const showConfirm = (id: number) => {
      names.splice(0);
      getDeleteNames(level1.value, id);
      Modal.confirm({
        title: 'Do you Want to delete these items?',
        icon: createVNode(ExclamationCircleOutlined),
        content: createVNode('div', { style: 'color:red;' }, 'The following will be deleted: '+names.join(", ")),
        onOk() {
          console.log('OK');
          handleDelete(id)
        },
        onCancel() {
          console.log('Cancel');
        },
        class: 'test',
      });
    };

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const ids: Array<string> = [];

    /**
     * 将某节点及其子孙节点全部删除
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {

          ids.push(id);

          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 某节点及其子孙节点的名称
     */


    const getDeleteNames = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {

          names.push(node.name);

          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteNames(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteNames(children, id);
          }
        }
      }
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
      treeSelectData,
      columns,
      loading,
      html,
      handleQuery,

      edit,
      add,
      modalVisible,
      modalLoading,
      handleSave,
      handleDelete,
      setDisable,
      getDeleteIds,
      showConfirm,
      getDeleteNames,
      handleContentQuery,
      onSelect

    };
  },
});

</script>

<style>
/**
wangeditor 默认样式
 */
  .wangeditor table {
    border-top: 1px solid #ccc;
    border-left: 1px solid #ccc;
  }
  .wangeditor table td,
  .wangeditor table th {
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
    padding: 3px 5px;
  }
  .wangeditor table th {
    border-bottom: 2px solid #ccc;
    text-align: center;
  }

  /*
  和antdv p冲突，覆盖掉
   */
  .wangeditor blockquote p {
    font-family: "YouYuan";
    margin: 20px 10px !important;
    font-size: 16px !important;
    font-weight: 600;
  }
</style>