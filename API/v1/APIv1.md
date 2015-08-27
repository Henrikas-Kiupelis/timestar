# API V1

[Back to README](/README.md)

## Below is a rough explanation of v1 API; this is not being updated and is deprecated/doesn't work anymore

PREFIX:

    http://{IP}:{PORT}
    
    POST /pictures/upload
    POST /documents/upload
    GET /pictures/{pictureName}
    GET /documents/{documentName}
    DELETE /pictures/{pictureName}
    DELETE /documents/{documentName}

PREFIX:

    http://{IP}:{PORT}/timestar/api
    
    /add - pridėti naują
    /{id} - rodyti pagal ID (žiūrėti šitą JSON formatui)
    /update - pataisyti (taisomos visos reikšmės išskyrūs ID!)
    /delete/{id} - ištrinti pagal ID
    /all - rodyti visus
    /{table}/{id} - rodyti visus tam tikram apribojimui, pvz /teacher/customer/1 - rodo visus mokytojus įmonei, kurios ID = 1

------------------------------------------------------------------------------------
Teacher:

    POST /teacher/add
    GET /teacher/{id}
    POST /teacher/update
    DELETE /teacher/delete/{id}
    GET /teacher/all

------------------------------------------------------------------------------------
Teacher language:

    POST /teacher/lang/add
    GET /teacher/lang/{teacherId}
    POST /teacher/lang/update
    GET /teacher/lang/delete/{teacherId}
    POST /teacher/lang/delete

------------------------------------------------------------------------------------
Teacher contract:

    POST /teacher/contract/add
    GET /teacher/contract/{teacherId}
    POST /teacher/contract/update
    GET /teacher/contract/delete/{teacherId}

------------------------------------------------------------------------------------
Customer:

    POST /customer/add
    GET /customer/{id}
    POST /customer/update
    GET /customer/delete/{id}
    GET /customer/teacher/{teacherId}
    GET /customer/all

------------------------------------------------------------------------------------
Customer contract:

    POST /customer/contract/add
    GET /customer/contract/{customerId}
    POST /customer/contract/update
    GET /customer/contract/delete/{customerId}

------------------------------------------------------------------------------------
Group:

    POST /group/add
    GET /group/{id}
    POST /group/update
    GET /group/delete/{id}
    GET /group/customer/{customerId}
    GET /group/teacher/{teacherId}
    GET /group/customer/{customerId}/teacher/{teacherId}
    GET /group/all

------------------------------------------------------------------------------------
Student:

    POST /student/add
    GET /student/{id}
    POST /student/update
    GET /student/delete/{id}
    GET /student/customer/{customerId}
    GET /student/group/{groupId}
    GET /student/lesson/{lessonId}

------------------------------------------------------------------------------------
Lesson:

    POST /lesson/add
    GET /lesson/{id}
    POST /lesson/update
    GET /lesson/delete/{id}
    
    GET /lesson/teacher/{teacherId}
    GET /lesson/customer/{customerId}
    GET /lesson/group/{groupId}
    optional: ?start={Date}&end={Date}
    /lesson/teacher/1
    /lesson/customer/1?start=2015-06-01
    /lesson/group/1?end=2015-06-30
    /lesson/teacher/1?start=2015-06-01&end=2015-06-30

------------------------------------------------------------------------------------
Attendance:

    POST lesson/attendance/add
    GET lesson/attendance/{lessonId}
    POST lesson/attendance/update
    GET lesson/attendance/delete/{lessonId}
    POST lesson/attendance/delete

------------------------------------------------------------------------------------
Lesson table:

    GET /lesson/table
    GET /lesson/table/{page}
    optional: ?per_page={int}&start={Date}&end={Date}
    /lesson/table
    /lesson/table/0?per_page=100
    /lesson/table/1?start=2015-06-26
    /lesson/table/100?start=2015-06-26&end=2015-07-26
