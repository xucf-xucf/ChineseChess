<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台010管理</title>
</head>
  <script type="text/javascript" src="../js/jquery.js"></script>

<link rel="stylesheet" href="../dist/css/bootstrap.min.css" />
<body>
					


				<div class="panel-body"
						style="width: 95%; height:45%; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
						<div style="text-align:center;">
						<input type="button" class="btn btn-warning" value="下一步"
						onclick="next();" />
						</div>
				</div>
				<div class="panel-body"
						style="width: 95%; height:45%; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
						<table class="table">
				<thead>
					<tr>
						<th>
							编号
						</th>
						<th>
							产品
						</th>
						<th>
							交付时间
						</th>
						<th>
							状态
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							1
						</td>
						<td>
							TB - Monthly
						</td>
						<td>
							01/04/2012
						</td>
						<td>
							Default
						</td>
					</tr>
					<tr class="success">
						<td>
							1
						</td>
						<td>
							TB - Monthly
						</td>
						<td>
							01/04/2012
						</td>
						<td>
							Approved
						</td>
					</tr>
					<tr class="error">
						<td>
							2
						</td>
						<td>
							TB - Monthly
						</td>
						<td>
							02/04/2012
						</td>
						<td>
							Declined
						</td>
					</tr>
					<tr class="warning">
						<td>
							3
						</td>
						<td>
							TB - Monthly
						</td>
						<td>
							03/04/2012
						</td>
						<td>
							Pending
						</td>
					</tr>
					<tr class="info">
						<td>
							4
						</td>
						<td>
							TB - Monthly
						</td>
						<td>
							04/04/2012
						</td>
						<td>
							Call in to confirm
						</td>
					</tr>
				</tbody>
			</table>
		
				</div>
</body>
<script type="text/javascript">
function next(){
	console.log(self.parent);
	self.parent.frames["leftFrame"].next();
}
</script>
</html>