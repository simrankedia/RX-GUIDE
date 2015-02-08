# -*- coding: utf-8 -*-
# this file is released under public domain and you can use without limitations

#########################################################################
## This is a sample controller
## - index is the default action of any application
## - user is required for authentication and authorization
## - download is for downloading files uploaded in the db (does streaming)
## - call exposes all registered services (none by default)
#########################################################################
import datetime
import os

def index():
    """
    example action using the internationalization operator T and flash
    rendered by views/default/index.html or views/generic.html

    if you need a simple wiki simply replace the two lines below with:
    return auth.wiki()
    """
    response.flash = T("Welcome to web2py!")
    return dict(message=T('Hello World'))


def user():
    """
    exposes:
    http://..../[app]/default/user/login
    http://..../[app]/default/user/logout
    http://..../[app]/default/user/register
    http://..../[app]/default/user/profile
    http://..../[app]/default/user/retrieve_password
    http://..../[app]/default/user/change_password
    http://..../[app]/default/user/manage_users (requires membership in
    use @auth.requires_login()
        @auth.requires_membership('group name')
        @auth.requires_permission('read','table name',record_id)
    to decorate functions that need access control
    """
    return dict(form=auth())

@cache.action()
def download():
    """
    allows downloading of uploaded files
    http://..../[app]/default/download/[filename]
    """
    return response.download(request, db)


def call():
    """
    exposes services. for example:
    http://..../[app]/default/call/jsonrpc
    decorate with @services.jsonrpc the functions to expose
    supports xml, json, xmlrpc, jsonrpc, amfrpc, rss, csv
    """
    return service()


@auth.requires_signature()
def data():
    """
    http://..../[app]/default/data/tables
    http://..../[app]/default/data/create/[table]
    http://..../[app]/default/data/read/[table]/[id]
    http://..../[app]/default/data/update/[table]/[id]
    http://..../[app]/default/data/delete/[table]/[id]
    http://..../[app]/default/data/select/[table]
    http://..../[app]/default/data/search/[table]
    but URLs must be signed, i.e. linked with
      A('table',_href=URL('data/tables',user_signature=True))
    or with the signed load operator
      LOAD('default','data.load',args='tables',ajax=True,user_signature=True)
    """
    return dict(form=crud())

def basic():
    num=int(request.vars.num)
    details=db(db.patient_details.id==num).select()
    details=details[0]
    now = datetime.datetime.now()
    age=now.year-int((details['dob'].split('-'))[0])
    allergies=details['allergies']
    allergies=allergies.replace('\n','\\n')
    allergies=allergies.replace(' ','_')
    return dict(details=details,age=age,allergies=allergies)

def add_img():
    form=SQLFORM(db.prescription)
    if form.process(session=None).accepted:
            redirect('add_img')
    return dict(form=form)

def add_new():
    if (request.vars):
        allergies=str(request.vars.allergies)
        allergies=allergies.replace('\\n','\n')
        allergies=allergies.replace('_',' ')
        name=str(request.vars.name)
        if name[-1]=='_':
            name=name[:-1]
        sex=str(request.vars.sex)
        if sex[-1]=='_':
            sex=sex[:-1]

        idno=db.patient_details.insert(patient_name=name,sex=sex,contact=long(request.vars.contact),allergies=allergies,dob=(str(request.vars.yy)+"-"+str(request.vars.mm)+"-"+str(request.vars.dd)))
        return dict(idno=idno)
    return dict()

def view_patients():
    patients=SQLFORM.grid(db.patient_details,user_signature=False)
    return dict(patients=patients)

def view_prescriptions():
    if request.vars:
        prescriptions=db(db.prescription.patient_id==int(request.vars.idno)).select()
    else:
        prescriptions=db(db.prescription.id>0).select()

    return dict(prescriptions=prescriptions)

def prescription_PDF():
    import time
    uid=int(request.vars.uid)
    name=str(request.vars.name)
    name=name.replace('_',' ')
    age=str(request.vars.age)
    sex=str(request.vars.sex)
    doc=str(request.vars.doctor)
    hname=str(request.vars.hname)
    allergy=str(request.vars.allergy)
    allergy=allergy.replace('\\n','\n')
    allergy=allergy.replace('_',' ')
    name=name.replace('_',' ')
    doc=doc.replace('_',' ')
    from gluon.contrib.pyfpdf import FPDF, HTMLMixin
    pdf=FPDF()
    pdf.add_page()
    pdf.set_font('Arial','',30)
    pdf.set_text_color(0,0,0)
    pdf.cell(w=200,h=30,txt=hname,ln=2,border=2,align='C',fill='0')
    pdf.set_font('Arial','',15)
    pdf.set_text_color(0,0,0)
    pdf.cell(w=0,h=10,txt="Name: "+name,border=0,align='L',fill='0')
    pdf.cell(w=-150,h=10,txt='Age: '+age,border=0,align='C',fill='0')
    pdf.cell(w=0,h=10,txt="Gender: "+sex,border=0,align='R',fill='0')
    pdf.cell(w=30,h=10,txt="",ln=1,border=0,align='L',fill='0')
    pdf.cell(w=30,h=10,txt="Allergies: ",border=0,align='L',fill='0')
    pdf.cell(w=3*len(allergy),h=10,txt=allergy,border=0,align='L',fill='0')
    pdf.cell(w=30,h=10,txt="",ln=1,border=0,align='L',fill='0')
    img=db(db.prescription.patient_id==uid).select()
    x=img[-1]['prescription_image']
    source=os.path.join(request.folder, 'uploads',x)
    pdf.image(name=source,w=170,h=170,type=(x.split('.'))[-1])
    pdf.cell(w=30,h=10,txt="",ln=1,border=0,align='L',fill='0')
    pdf.set_font('Arial','',15)
    pdf.set_text_color(0,0,0)
    pdf.cell(w=0,h=10,txt="Date: "+time.strftime("%d/%m/%Y"),border=0,align='L',fill='0')
    pdf.cell(w=-20,h=10,txt=doc,border=0,align='R',fill='0')
    pdf.cell(w=30,h=10,txt="",ln=1,border=0,align='R',fill='0')
    pdf.cell(w=30,h=10,txt="",ln=1,border=0,align='R',fill='0')
    pdf.cell(w=120,h=10,txt="Powered by RXGUIDE",border=0,align='R',fill='0')
    response.headers['Content-Type']='application/pdf'
    return pdf.output(name='prescription.pdf',dest="S")