<template>
  <a-layout-header class="header">
    <div class="logo">WiKi知识库</div>
    <a-menu
        theme="dark"
        mode="horizontal"
        :default-selected-keys="['2']"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">
        <router-link to="/about">About</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to="/admin/ebook" :style="user.id?{}:{display: 'none'}">EbookAdmin</router-link>
      </a-menu-item>
      <a-menu-item key="4">
        <router-link to="/admin/category" :style="user.id?{}:{display: 'none'}">CategoryAdmin</router-link>
      </a-menu-item>
      <a-menu-item key="5">
        <router-link to="/admin/user" :style="user.id?{}:{display: 'none'}">UserAdmin</router-link>
      </a-menu-item>

      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="!!user.name">
          <span>退出登录</span>
        </a>
      </a-popconfirm>

      <a class="login-menu" v-show="!!user.name">
        <span>您好：{{user.name}}</span>
      </a>

      <a class="login-menu" @click="showLoginModal" v-show="!user.name">
        <span>login</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  props: {
    msg: String,
  },

  setup () {
    // 用来登录
    const loginUser = ref({
      loginName: "test",
      password: "test"
    });

    //登陆后保存
    const user = computed(() => store.state.user);

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/User/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser",data.content);

        } else {
          message.error(data.message);
        }
      });
    };
    //  退出登录
    const logout = () => {
      console.log("退出登录开始");

      axios.get('/User/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出成功！");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };



    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      logout,
      user
    }
  }
});
</script>

<style>
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}

</style>