<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input
          v-model="searchModel.departmentName"
          placeholder="请输入部门名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search()"
          >查询</el-button
        >
        <el-button icon="el-icon-refresh-right" @click="resetValue()">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openAddWindow()"
          >新增</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      :data="tableData"
      style="width: 100%; margin-bottom: 20px"
      row-key="id"
      border
      stripe
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <!-- prop属性：填写数据的属性名称 -->
      <!-- label属性：表格表头标题 -->
      <el-table-column prop="departmentName" label="部门名称" />
      <el-table-column prop="parentName" label="所属部门" />
      <el-table-column prop="phone" label="部门电话" />
      <el-table-column prop="address" label="部门地址" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            icon="el-icon-edit-outline"
            type="primary"
            @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-button
            icon="el-icon-close"
            type="danger"
            size="small"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加和修改窗口 -->
    <system-dialog
      :title="deptDialog.title"
      :visible="deptDialog.visible"
      :width="deptDialog.width"
      :height="deptDialog.height"
      @onClose="onClose()"
      @onConfirm="onConfirm()"
    >
      <div slot="content">
        <el-form
          :model="dept"
          ref="deptForm"
          :rules="rules"
          label-width="80px"
          :inline="true"
          size="small"
        >
          <el-form-item label="所属部门" prop="parentName">
            <el-input
              v-model="dept.parentName"
              @click.native="openSelectDeptWindow()"
              :readonly="true"
            ></el-input>
          </el-form-item>
          <el-form-item label="部门名称" prop="departmentName">
            <el-input v-model="dept.departmentName"></el-input>
          </el-form-item>
          <el-form-item label="部门电话">
            <el-input v-model="dept.phone"></el-input>
          </el-form-item>
          <el-form-item label="部门地址">
            <el-input v-model="dept.address"></el-input>
          </el-form-item>
          <el-form-item label="序号">
            <el-input v-model="dept.orderNum"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </system-dialog>
    <!-- 选择所属部门的窗口 -->
    <system-dialog
      :title="parentDialog.title"
      :visible="parentDialog.visible"
      :width="parentDialog.width"
      :height="parentDialog.height"
      @onClose="onParentClose()"
      @onConfirm="onParentConfirm()"
    >
      <div slot="content">
        <el-tree
          ref="parentTree"
          :data="treeList"
          node-key="id"
          :props="defaultProps"
          empty-text="暂无数据"
          :highlight-current="true"
          :default-expand-all="true"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        >
          <div class="customer-tree-node" slot-scope="{ node, data }">
            <span v-if="data.children.length === 0">
              <i class="el-icon-document"></i>
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
// 导入department脚本文件
import departmentApi from '@/api/department';
// 导入SystemDialog组件
import SystemDialog from '@/components/system/SystemDialog.vue';
export default {
  components: { SystemDialog },
  name: 'department',
  data() {
    return {
      searchModel: {
        departmentName: ''
      },
      tableData: [],
      deptDialog: {
        title: '新增部门',
        visible: false, //是否显示
        width: 560,
        height: 170
      },
      //部门对象
      dept: {
        id: '',
        pid: '',
        parentName: '',
        departmentName: '',
        address: '',
        phone: '',
        orderNum: ''
      },
      //表单验证规则
      rules: {
        parentName: [
          { required: true, trigger: 'change', message: '请选择所属部门' }
        ],
        departmentName: [
          { required: true, trigger: 'blur', message: '请输入部门名称' }
        ]
      },
      // 选择所属部门的属性
      parentDialog: {
        title: '选择所属部门',
        visible: false, //是否显示
        width: 300,
        height: 400
      },
      treeList: [], //树形数据
      defaultProps: {
        children: 'children',
        label: 'departmentName'
      }
    };
  },
  created() {
    this.search();
  },
  methods: {
    /**
     * 弹窗取消事件
     */
    onClose() {
      //关闭窗口
      this.deptDialog.visible = false;
    },
    /**
     * 弹窗确认事件
     */
    onConfirm() {
      //表单验证
      this.$refs.deptForm.validate(async (valid) => {
        //如果验证通过
        if (valid) {
          // 发送添加请求
          let res = null
          // 判断当前是新增还是修改(判断当前dept的id是否为空)
          if(this.dept.id === ""){
            // 发送新增请求
            res = await departmentApi.addDept(this.dept)
          }else{
            // 发送修改请求
            res = await departmentApi.updateDept(this.dept)
          }
          if(res.success){
            // 提示成功
            this.$message.success(res.message)
            // 刷新数据
            this.search()
            //关闭窗口
            this.deptDialog.visible = false;
          }else{
            // 提示成功
            this.$message.error(res.message)
          }
        }
      });
    },
    /**
     * 选择的所属部门的关闭事件
     */
    onParentClose() {
      this.parentDialog.visible = false;
    },
    /**
     * 选择所属部门的确认事件
     */
    onParentConfirm() {
      this.parentDialog.visible = false;
    },
    /**
     * 编辑部门
     */
    handleEdit(row){
      // 数据回显
      this.$objCopy(row,this.dept)
      // 设置窗口标题
      this.deptDialog.title = "编辑部门"
      // 显示窗口
      this.deptDialog.visible = true
    },
    /**
     * 删除部门
     */
    async handleDelete(row){
      // 查询部门下是否有子部门或用户
      let result = await departmentApi.checkDepartment({ id: row.id });
      // 判断是否可以删除
      if (!result.success) {
        //提示不能删除
        this.$message.warning(result.message);
      } else {
        //确认是否删除
        let confirm =await this.$myconfirm("确定要删除该数据吗?");
        if (confirm) {
          //发送删除请求
          let res = await departmentApi.deleteById({ id: row.id });
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
    },
    /**
     * 打开所属部门的窗口
     */
    async openSelectDeptWindow() {
      this.parentDialog.visible = true;
      // 查询所属部门列表
      let res = await departmentApi.getParentTreeList();
      // 判断是否成功
      if (res.success) {
        this.treeList = res.data;
      }
    },
    // 树形收缩图标改变
    changeIcon(data){
      data.open = !data.open
      this.$refs.parentTree.store.nodesMap[data.id].expanded = !data.open
    },
    /**
     * 树节点点击事件
     */
    handleNodeClick(data) {
      //当点击树节点时，赋予选中的值
      this.dept.pid = data.id;
      this.dept.parentName = data.departmentName;
    },
    /**
     * 打开添加部门窗口
     */
    openAddWindow() {
      // 清空表单数据
      this.$resetForm("deptForm",this.dept)
      //设置窗口标题
      this.deptDialog.title = '新增部门';
      //显示添加部门窗口
      this.deptDialog.visible = true;
    },
    /**
     * 选择所属部门
     */
    selectDepartment() {},
    async search() {
      let res = await departmentApi.getDepartmentList(this.searchModel);
      if (res.success) {
        this.tableData = res.data;
      }
    },
    /**
     * 重置查询条件
     */
    resetValue() {
      // 清空数据
      this.searchModel.departmentName = ""
      // 查询
      this.search()
    },
  }
};
</script>
<style lang="scss" scoped>
::v-deep .el-tree {
  .el-tree-node {
    position: relative;
    padding-left: 10px;
  }
  .el-tree-node__children {
    padding-left: 20px;
  }
  .el-tree-node :last-child:before {
    height: 40px;
  }
  .el-tree > .el-tree-node:before {
    border-left: none;
  }
  .el-tree > .el-tree-node:after {
    border-top: none;
  }
  .el-tree-node:before,
  .el-tree-node:after {
    content: '';
    left: -4px;
    position: absolute;
    right: auto;
    border-width: 1px;
  }
  .tree :first-child .el-tree-node:before {
    border-left: none;
  }
  .el-tree-node:before {
    border-left: 1px dotted #d9d9d9;
    bottom: 0px;
    height: 100%;
    top: -25px;
    width: 1px;
  }
  .el-tree-node:after {
    border-top: 1px dotted #d9d9d9;
    height: 20px;
    top: 14px;
    width: 24px;
  }
  .el-tree-node__expand-icon.is-leaf {
    width: 8px;
  }
  .el-tree-node__content > .el-tree-node__expand-icon {
    display: none;
  }
  .el-tree-node__content {
    line-height: 30px;
    height: 30px;
    padding-left: 10px !important;
  }
}
::v-deep .el-tree > div {
  &::before {
    display: none;
  }
  &::after {
    display: none;
  }
}
</style>