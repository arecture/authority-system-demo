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
          <el-tag v-if="scope.row.type == '0'" size="normal" align="center"
            >目录</el-tag
          >
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
            @click="handleEidt(scope.row)"
            >编辑
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增或修改窗口 -->
    <system-dialog
      :title="menuDialog.title"
      :visible="menuDialog.visible"
      :width="menuDialog.width"
      :height="menuDialog.height"
      @onClose="onClose()"
      @onConfirm="onConfirm()"
    >
      <div slot="content">
        <el-form
          :model="menu"
          ref="menuForm"
          :rules="rules"
          label-width="80px"
          :inline="true"
          size="small"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item prop="type" label="菜单类型">
                <el-radio-group v-model="menu.type">
                  <el-radio :label="0">目录</el-radio>
                  <el-radio :label="1">菜单</el-radio>
                  <el-radio :label="2">按钮</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item prop="parentName" size="small" label="所属菜单">
            <el-input
              @click.native="selectParentMenu"
              v-model="menu.parentName"
              :readonly="true"
            ></el-input>
          </el-form-item>
          <el-form-item prop="label" size="small" label="菜单名称">
            <el-input v-model="menu.label"></el-input>
          </el-form-item>
          <el-form-item
            prop="name"
            v-if="menu.type == 1"
            size="small"
            label="路由名称"
          >
            <el-input v-model="menu.name"></el-input>
          </el-form-item>
          <el-form-item
            prop="path"
            v-if="menu.type != 2"
            size="small"
            label="路由地址"
          >
            <el-input v-model="menu.path"></el-input>
          </el-form-item>
          <el-form-item
            prop="url"
            v-if="menu.type == 1"
            size="small"
            label="组件路径"
          >
            <el-input v-model="menu.url"></el-input>
          </el-form-item>
          <el-form-item prop="code" size="small" label="权限字段">
            <el-input v-model="menu.code"></el-input>
          </el-form-item>
          <el-form-item size="small" label="菜单图标">
            <my-icon @selecticon="setIcon" ref="child" />
          </el-form-item>
          <el-form-item size="small" label="菜单序号">
            <el-input v-model="menu.orderNum"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </system-dialog>
    <system-dialog
      :title="parentDialog.title"
      :width="parentDialog.width"
      :height="parentDialog.height"
      :visible="parentDialog.visible"
      @onClose="onParentClose()"
      @onConfirm="onParentConfirm()"
    >
      <div slot="content">
        <el-tree
          ref="parentTree"
          :data="parentMenuList"
          node-key="id"
          :props="defaultProps"
          style="font-size: 14px"
          empty-text="暂无数据"
          :highlight-current="true"
          :default-expand-all="true"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        >
          <div class="customer-tree-node" slot-scope="{ node, data }">
            <!-- 长度为0说明没有下级 -->
            <span v-if="data.children.length === 0">
              <i
                class="el-icon-document"
                style="color: #8c8c8c; font-size: 18px"
              ></i>
            </span>
            <span v-else @click="changeIcon(data)">
              <svg-icon v-if="data.open" icon-class="circle-add" />
              <svg-icon v-else icon-class="circle-subtract" />
            </span>
            <span style="margin-left: 3px">{{ node.label }}</span>
          </div>
        </el-tree>
      </div>
    </system-dialog>
  </el-main>
</template>

<script>
import menuApi from '@/api/menu';

//导入对话框组件
import SystemDialog from '@/components/system/SystemDialog.vue';

//导入自定义图标组件
import MyIcon from '@/components/system/MyIcon.vue';

export default {
  name: 'menuList',
  //注册组件
  components: {
    SystemDialog,
    MyIcon
  },
  data() {
    return {
      menuList: [], //数据列表
      tableHeight: 0, //表格高度
      menuDialog: {
        title: '新增菜单',
        visible: false,
        width: 630,
        height: 270
      },
      menu: {
        id: '',
        type: '',
        parentId: '',
        parentName: '',
        label: '',
        icon: '',
        name: '',
        path: '',
        url: '',
        code: '',
        orderNum: ''
      },
      rules: {
        type: [
          { required: true, trigger: 'change', message: '请选择菜单类型' }
        ],
        parentName: [
          { required: true, trigger: 'change', message: '请选择所属菜单' }
        ],
        label: [{ required: true, trigger: 'blur', message: '请输入菜单名称' }],
        name: [{ required: true, trigger: 'blur', message: '请输入路由名称' }],
        path: [{ required: true, trigger: 'blur', message: '请输入路由路径' }],
        url: [{ required: true, trigger: 'blur', message: '请输入组件路径' }],
        code: [{ required: true, trigger: 'blur', message: '请输入权限字段' }]
      },
      // 上级菜单弹窗属性
      parentDialog: {
        title: '选择所属菜单',
        width: 280,
        height: 450,
        visible: false
      },
      //树属性定义
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      parentMenuList: [] //所属菜单列表
    };
  },
  // 初始化时调用
  created() {
    // 查询菜单列表
    this.search();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 180;
    });
  },
  methods: {
    /**
     * 查询菜单列表
     */
    async search() {
      // 发送菜单请求
      let res = await menuApi.getMenuList();
      if (res.success) {
        this.menuList = res.data;
      }
    },
    /**
     * 选择所属菜单
     */
    async selectParentMenu() {
      this.parentDialog.visible = true;
      let res = await menuApi.getParentMenuList();
      if (res.success) {
        this.parentMenuList = res.data;
      }
    },
    /**
     * 选择所属菜单取消事件
     */
    onParentClose() {
      this.parentDialog.visible = false; //关闭窗口
    },
    /**
     * 选择所属菜单确认事件
     */
    onParentConfirm() {
      this.parentDialog.visible = false; //关闭窗口
    },
    changeIcon(data) {
      data.open = !data.open;
      this.$refs.parentTree.store.nodesMap[data.id].expanded = !data.open;
    },
    /**
     * 所属菜单节点点击事件
     */
    handleNodeClick(data) {
      //所属父级菜单ID
      this.menu.parentId = data.id;
      //所属父级菜单名称
      this.menu.parentName = data.label;
    },
    /**
     * 设置选择的图标
     */
    setIcon(icon) {
      this.menu.icon = icon;
    },
    // 打开新增窗口
    openAddWindow() {
      this.$resetForm('menuForm', this.menu); //清空表单数据
      this.menuDialog.title = '新增菜单'; //设置窗口标题
      this.menuDialog.visible = true; //显示窗口
      this.$$nextTick(() => {
        this.$$refs.child.userChooseIcon = '';
      }); //清空菜单图标
    },
    /**
     * 关闭取消按钮事件
     */
    onClose() {
      this.menuDialog.visible = false;
    },
    /**
     * 确认按钮事件
     */
    onConfirm() {
      this.$refs.menuForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          // 判断是新增操作还是修改操作
          if (this.menu.id === '') {
            // 发送添加请求
            res = await menuApi.addMenu(this.menu);
          } else {
            // 发送修改请求
            res = await menuApi.updateMenu(this.menu);
          }
          // 判断是否成功
          if (res.success) {
            // 提示成功
            this.$message.success(res.message);
            // 刷新数据
            this.search();
            // 关闭窗口
            this.menuDialog.visible = false;
          } else {
            // 提示失败
            this.$message.error(res.message);
          }
        }
      });
    },
    /**
     * 打开编辑菜单窗口
     */
    handleEidt(row) {
      // 数据回显
      this.$objCopy(row, this.menu);
      //设置弹框属性
      this.menuDialog.title = '编辑菜单';
      this.menuDialog.visible = true;
      this.$nextTick(() => {
        this.$refs.child.userChooseIcon = row.icon //菜单图标回显
      })
    },
    /**
     * 删除菜单
     */
    async handleDelete(row){
      // 判断是否存在子菜单
      let result = await menuApi.checkPermission({ id: row.id });
      // 判断是否可以删除
      if (!result.success) {
        //提示不能删除
        this.$message.warning(result.message);
      } else {
        //确认是否删除
        let confirm = await this.$myconfirm("确定要删除该数据吗?");
        if (confirm) {
          //发送删除请求
          let res = await menuApi.deleteById({ id: row.id });
          //判断是否成功
          if (res.success) {
            //成功提示
            this.$message.success(res.message);
            //刷新
            this.search();
          } else {
            //失败提示
            this.$message.error(res.message);
          }
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.iconList {
  width: 400px;
  height: 300px;
  overflow-y: scroll;
  overflow-x: hidden;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  i {
    display: inline-block;
    width: 60px;
    height: 45px;
    color: #000000;
    font-size: 20px;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    cursor: pointer;
    text-align: center;
    line-height: 45px;
    margin: 5px;
    &:hover {
      color: orange;
      border-color: orange;
    }
  }
}
.chooseIcons {
  width: 175px;
  background-color: #ffffff;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: inherit;
  height: 33px;
  line-height: 25px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}
</style>