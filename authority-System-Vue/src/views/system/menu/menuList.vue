<template>
  <el-main>
    <!-- 新增按钮 -->
    <el-button
      type="success"
      size="small"
      @click="openAddWindow()"
      icon="el-icon-plus"
      >新增</el-button
    >
    <!-- 数据表格 -->
    <el-table
      style="margin-top: 10px"
      :height="tableHeight"
      :data="menuList"
      row-key="id"
      default-expand-all
      :tree-props="{ children: 'children' }"
      :border="true"
      stripe
    >
      <el-table-column prop="label" label="菜单名称"></el-table-column>
      <el-table-column prop="type" label="菜单类型" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == '0'" size="normal" align="center">目录</el-tag>
          <el-tag type="success" v-else-if="scope.row.type == '1'" size="normal"
            >菜单</el-tag
          >
          <el-tag type="warning" v-else-if="scope.row.type == '2'" size="normal"
            >按钮</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" align="center">
        <template slot-scope="scope">
          <i
            :class="scope.row.icon"
            v-if="scope.row.icon.includes('el-icon')"
          ></i>
          <svg-icon v-else :icon-class="scope.row.icon"></svg-icon>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="路由名称"></el-table-column>
      <el-table-column prop="path" label="路由地址"></el-table-column>
      <el-table-column prop="url" label="组件路径"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="small"
            @click="editMenu(scope.row)"
            >编辑
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="deleteMenu(scope.row)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-main>
</template>

<script>
import menuApi from '@/api/menu'
export default {
  name:"menuList",
  data() {
    return {
      menuList:[],//数据列表
      tableHeight:0,
    }
  },
  // 初始化时调用
  created() {
    // 查询菜单列表
    this.search();
  },
  mounted() {
      this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 180
    })
  },
  methods: {
    /**
     * 查询菜单列表
     */
    async search(){
      // 发送菜单请求
      let res = await menuApi.getMenuList();
      console.log(res)
      if (res.success) {
        this.menuList = res.data
      }
    },

    // 打开新增窗口
    openAddWindow(){

    }
  },
};
</script>

<style lang="scss" scoped></style>