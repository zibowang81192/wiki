<template>
  <a-layout class="box">
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys"
          mode="vertical"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <router-link to="/">
            <MailOutlined />
            <span>欢迎</span>
          </router-link>

        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined/>{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutLined/><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-list item-layout="vertical" size="large"  :grid="{ gutter: 25, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>

    </a-layout-content>
  </a-layout>

</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined, MailOutlined} from '@ant-design/icons-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";

// const listData: Record<string, string>[] = [];
//
// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: 'https://www.antdv.com/',
//     title: `ant design vue part ${i}`,
//     avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
//     description:
//         'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//     content:
//         'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//   });
// }

export default defineComponent({
  name: 'Home',

  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined
  },

  setup() {
    console.log("setup");
    const level1 = ref();
    const ebooks = ref();
    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '7' },
    ];

    const handleQuery=(params:any)=>{
      axios.get("/Ebook/list",
          {params: {
              page: params.page,
              size: params.size
            }}).then((response)=>{
        const data = response.data;
        ebooks.value = data.content.list;

      });
    };

    let categorys: any;

    const handleCategoryQuery=()=> {
      axios.get("/Category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组: ", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

        } else {
          message.error(data.message);
        }

      });
    };
      onMounted(()=>{
        console.log("onMounted");
        handleCategoryQuery();
        handleQuery({
          page: 1,
          size: 1000
        })
        // axios.get("/Ebook/list").then((response)=>{
        //   const data = response.data;
        //   ebooks.value = data.content;
        //   console.log(response);
        // });
      })

    return {
      ebooks,
      pagination,
      actions,
      level1,

      handleCategoryQuery
    };
  },
});

</script>
<style>
.box {
  display: flex;
}
</style>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
